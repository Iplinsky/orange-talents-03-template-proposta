package br.com.zupacademy.proposta.controller;

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
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.PropostaRequest;
import br.com.zupacademy.proposta.models.response.PropostaResponse;
import br.com.zupacademy.proposta.repository.PropostaRepository;
import br.com.zupacademy.proposta.utils.AvaliaProposta;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

	private PropostaRepository propostaRepository;
	private ExecutarTransacao executaTransacao;
	private VerificaRestricaoFinanceira verificaRestricaoFinanceira;

	public PropostaController(PropostaRepository propostaRepository, ExecutarTransacao executaTransacao,
			VerificaRestricaoFinanceira verificaRestricaoFinanceira) {
		this.propostaRepository = propostaRepository;
		this.executaTransacao = executaTransacao;
		this.verificaRestricaoFinanceira = verificaRestricaoFinanceira;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> cadastrarProposta(@Valid @RequestBody PropostaRequest request,
			UriComponentsBuilder uriBuilder) {

		Proposta proposta = request.toModel();

		boolean existeProposta = propostaRepository.existsPropostaByDocumento(proposta.getDocumento());

		if (existeProposta)
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY,
					"Já existe uma proposta relacionada a este documento!");

		executaTransacao.salvarRegistro(proposta);
		proposta = AvaliaProposta.verificaSeExisteRestricaoFinanceira(proposta, verificaRestricaoFinanceira);
		executaTransacao.atualizarRegistro(proposta);

		return ResponseEntity.created(uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri()).build();
	}

	@GetMapping("/consulta/{id}")
	public ResponseEntity<PropostaResponse> consultarProposta(@PathVariable Long id) {
		Optional<Proposta> propostaLocalizada = propostaRepository.findById(id);

		return propostaLocalizada.map(proposta -> {
			return ResponseEntity.ok(new PropostaResponse(propostaLocalizada.get()));
		}).orElse(ResponseEntity.notFound().build());
	}

}