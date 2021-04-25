package br.com.zupacademy.proposta.controller;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zupacademy.proposta.metrics.PrometheusMetrics;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CarteiraDigital;
import br.com.zupacademy.proposta.models.request.CarteiraDigitalRequest;
import br.com.zupacademy.proposta.repository.CartaoRepository;
import br.com.zupacademy.proposta.utils.AvaliaAssociacaoComCarteiraDigital;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/carteiras/cartao")
public class CarteiraDigitalController {

	private final PrometheusMetrics metrics;
	private final CartaoRepository cartaoRepository;
	private final ExecutarTransacao executarTransacao;
	private final Tracer tracer;

	public CarteiraDigitalController(CartaoRepository cartaoRepository, ExecutarTransacao executarTransacao,
			PrometheusMetrics metrics, Tracer tracer) {
		this.metrics = metrics;
		this.cartaoRepository = cartaoRepository;
		this.executarTransacao = executarTransacao;
		this.tracer = tracer;
	}

	@PostMapping("/{id}")
	@Transactional
	public ResponseEntity<?> cadastrarCarteiraDigital(@PathVariable("id") Long id,
			@Valid @RequestBody CarteiraDigitalRequest request) {
		Long initialTime = System.currentTimeMillis();
		
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("cartao.id", id);
		activeSpan.log(Map.of("event", "iniciando cadastro de carteira digital para o cartao de id: " + id));

		Optional<Cartao> cartao = cartaoRepository.findById(id);

		return cartao.map(card -> {

			CarteiraDigital carteiraDigital = request.toModel(card);
			AvaliaAssociacaoComCarteiraDigital.validarAssociacao(card, carteiraDigital, executarTransacao, tracer);
			executarTransacao.salvarRegistro(carteiraDigital);

			metrics.timer("timer_cartao_digital", initialTime);
			metrics.counter("cartao_digital_criado");
			
			activeSpan.log(Map.of("event", "finalizando o processo de cadastro da carteira digital para o cartao de id: " + id + " com sucesso."));

			return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
					.buildAndExpand(carteiraDigital.getId()).toUri()).build();

		}).orElse(ResponseEntity.notFound().build());
	}

}
