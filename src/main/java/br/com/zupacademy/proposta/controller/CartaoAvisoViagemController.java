package br.com.zupacademy.proposta.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zupacademy.proposta.metrics.PrometheusMetrics;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoAvisoViagem;
import br.com.zupacademy.proposta.models.request.CartaoAvisoViagemRequest;
import br.com.zupacademy.proposta.repository.CartaoRepository;
import br.com.zupacademy.proposta.utils.AvaliaComunicacaoAvisoDeViagem;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
public class CartaoAvisoViagemController {

	private final CartaoRepository cartaoRepository;
	private final ExecutarTransacao executarTransacao;
	private final PrometheusMetrics metrics;
	private final Tracer tracer;

	public CartaoAvisoViagemController(CartaoRepository cartaoRepository, ExecutarTransacao executarTransacao,
			PrometheusMetrics metrics, Tracer tracer) {
		this.cartaoRepository = cartaoRepository;
		this.executarTransacao = executarTransacao;
		this.metrics = metrics;
		this.tracer = tracer;
	}

	@PostMapping("/cartao/{id}/aviso-viagem")
	@Transactional
	public ResponseEntity<?> cadastrarAvisoDeViagem(@PathVariable("id") Long idCartao,
			@Valid @RequestBody CartaoAvisoViagemRequest cartaoRequest, HttpServletRequest request) {
		Long initialTime = System.currentTimeMillis();
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("cartao.id", idCartao);
		activeSpan.log(Map.of("event", "iniciando o processo aviso de viagem para o cartao de id: " + idCartao));

		Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

		return cartao.map(card -> {
			CartaoAvisoViagem cartaoAvisoViagem = cartaoRequest.toModel(
					Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr()),
					request.getHeader("User-Agent"), card);

			AvaliaComunicacaoAvisoDeViagem.validaAvisoDeViagem(card, cartaoAvisoViagem, executarTransacao, tracer);

			executarTransacao.salvarRegistro(cartaoAvisoViagem);

			metrics.timer("timer_aviso_viagem", initialTime);
			metrics.counter("aviso_viagem_criado");

			activeSpan.log(Map.of("event", "finalizando o processo aviso de viagem para o cartao de id: " + idCartao + "com sucesso."));
			
			return ResponseEntity.ok().header("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/{id}").buildAndExpand(cartaoAvisoViagem.getId()).toUri().toString()).build();

		}).orElse(ResponseEntity.notFound().build());
	}

}
