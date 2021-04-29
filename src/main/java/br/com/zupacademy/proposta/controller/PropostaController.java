package br.com.zupacademy.proposta.controller;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.proposta.feign.VerificaRestricaoFinanceira;
import br.com.zupacademy.proposta.handler.exception.ApiErroException;
import br.com.zupacademy.proposta.metrics.PrometheusMetrics;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.PropostaRequest;
import br.com.zupacademy.proposta.models.response.PropostaResponse;
import br.com.zupacademy.proposta.repository.PropostaRepository;
import br.com.zupacademy.proposta.utils.AvaliaProposta;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

	private final PropostaRepository propostaRepository;
	private final ExecutarTransacao executaTransacao;
	private final VerificaRestricaoFinanceira verificaRestricaoFinanceira;
	private final PrometheusMetrics metrics;
	private final Tracer tracer;

	public PropostaController(PropostaRepository propostaRepository, ExecutarTransacao executaTransacao,
			VerificaRestricaoFinanceira verificaRestricaoFinanceira, PrometheusMetrics metrics, Tracer tracer) {
		this.propostaRepository = propostaRepository;
		this.executaTransacao = executaTransacao;
		this.verificaRestricaoFinanceira = verificaRestricaoFinanceira;
		this.metrics = metrics;
		this.tracer = tracer;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrarProposta(@Valid @RequestBody PropostaRequest request, UriComponentsBuilder uriBuilder) {

		Long initialTime = System.currentTimeMillis();
		Span activeSpan = tracer.activeSpan();

		activeSpan.setTag("user.email", request.getEmail());
		activeSpan.log(Map.of("event", "iniciando cadastro de proposta para o usuario " + request.getEmail()));

		/*
		 * NECESSÁRIO RECUPERAR OS DOCUMENTOS DO BANCO DE DADOS E DESCRIPTOGRAFA-LOS
		 * PARA SER FEITA A COMPARAÇÃO COM O VALOR DO DOCUMENTO RECEBIDO PELA REQUISIÇÃO.
		 */
		long counterExistsProposta = propostaRepository.findAll().stream().filter(pr -> pr.getDocumentoDecrypt().equalsIgnoreCase(request.getDocumento())).count();

		if (counterExistsProposta > 0)
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY,
					"Já existe uma proposta relacionada a este documento!");

		Proposta proposta = request.toModel();

		executaTransacao.salvarRegistro(proposta);
		proposta = AvaliaProposta.verificaSeExisteRestricaoFinanceira(proposta, verificaRestricaoFinanceira, tracer);
		executaTransacao.atualizarRegistro(proposta);

		metrics.timer("timer_proposta", initialTime);
		metrics.counter("proposta_criada");

		activeSpan.log(Map.of("event",
				"processo de cadastro da proposta finalizado para o usuario " + request.getEmail() + " com sucesso."));

		return ResponseEntity.created(uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri())
				.build();
	}

	@GetMapping("/consulta/{id}")
	public ResponseEntity<PropostaResponse> consultarProposta(@PathVariable Long id) {
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("id.proposta", id);
		activeSpan.log(Map.of("event", "consultando proposta de id " + id));

		Optional<Proposta> propostaLocalizada = propostaRepository.findById(id);

		return propostaLocalizada.map(proposta -> {
			activeSpan.log(
					Map.of("event", "finalizando o processo de consulta a proposta de id " + id + " com sucesso."));
			return ResponseEntity.ok(new PropostaResponse(propostaLocalizada.get()));
		}).orElse(ResponseEntity.notFound().build());
	}

}