package br.com.zupacademy.proposta.controller;

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

import br.com.zupacademy.proposta.metrics.Metrics;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoAvisoViagem;
import br.com.zupacademy.proposta.models.request.CartaoAvisoViagemRequest;
import br.com.zupacademy.proposta.repository.CartaoRepository;
import br.com.zupacademy.proposta.utils.AvaliaComunicacaoAvisoDeViagem;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;

@RestController
public class CartaoAvisoViagemController {

	private final CartaoRepository cartaoRepository;
	private final ExecutarTransacao executarTransacao;
	private final Metrics metrics;

	public CartaoAvisoViagemController(CartaoRepository cartaoRepository, ExecutarTransacao executarTransacao, Metrics metrics) {
		this.cartaoRepository = cartaoRepository;
		this.executarTransacao = executarTransacao;
		this.metrics = metrics;
	}

	@PostMapping("/cartao/{id}/aviso-viagem")
	@Transactional
	public ResponseEntity<?> cadastrarAvisoDeViagem(@PathVariable("id") Long idCartao,
			@Valid @RequestBody CartaoAvisoViagemRequest cartaoRequest, HttpServletRequest request) {
		Long initialTime = System.currentTimeMillis();

		Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

		return cartao.map(card -> {
			CartaoAvisoViagem cartaoAvisoViagem = cartaoRequest.toModel(
					Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr()),
					request.getHeader("User-Agent"), card);

			AvaliaComunicacaoAvisoDeViagem.validaAvisoDeViagem(card, cartaoAvisoViagem, executarTransacao);

			executarTransacao.salvarRegistro(cartaoAvisoViagem);

			metrics.timer("timer_aviso_viagem", initialTime);
			metrics.counter("aviso_viagem_criado");

			return ResponseEntity.ok().header("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/{id}").buildAndExpand(cartaoAvisoViagem.getId()).toUri().toString()).build();

		}).orElse(ResponseEntity.notFound().build());
	}

}
