package br.com.zupacademy.proposta.controller;

import java.util.Map;

import javax.persistence.EntityManager;
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
import br.com.zupacademy.proposta.models.Biometria;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.request.BiometriaRequest;
import br.com.zupacademy.proposta.models.response.BiometriaResponse;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/biometrias")
public class BiometriaController {

	private final EntityManager em;
	private final PrometheusMetrics metrics;
	private final Tracer tracer;

	public BiometriaController(EntityManager em, PrometheusMetrics metrics, Tracer tracer) {
		this.em = em;
		this.metrics = metrics;
		this.tracer = tracer;
	}

	@PostMapping("/cartao/{id}")
	@Transactional
	public ResponseEntity<BiometriaResponse> cadastrarBiometria(@PathVariable("id") Long idCartao, @Valid @RequestBody BiometriaRequest bioRequest) {		
		Long initialTime = System.currentTimeMillis();
		
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("cartao.id", idCartao);
		activeSpan.log(Map.of("event", "iniciando o processo para cadastro de biometria para o usuario do cartao de id: " + idCartao));

		Cartao cartao = em.find(Cartao.class, idCartao);

		if (cartao == null)
			return ResponseEntity.notFound().build();

		Biometria biometria = bioRequest.toModel(cartao);
		cartao.associarBiometria(biometria);
		em.persist(biometria);
		em.merge(cartao);

		metrics.timer("timer_biometria", initialTime);
		metrics.counter("biometria_criada");

		activeSpan.log(Map.of("event", "finalizando o processo de cadastro da biometria para o usuario do cartao de id: " + idCartao + " com sucesso."));
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
				.buildAndExpand(biometria.getId()).toUri()).body(new BiometriaResponse(biometria));
	}

}
