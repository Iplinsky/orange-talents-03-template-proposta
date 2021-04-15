package br.com.zupacademy.proposta.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zupacademy.proposta.models.Biometria;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.request.BiometriaRequest;
import br.com.zupacademy.proposta.models.response.BiometriaResponse;

@RestController
@RequestMapping("/biometria")
public class BiometriaController {

	@PersistenceContext
	private EntityManager em;

	@PostMapping("/cartao/{id}")
	@Transactional
	public ResponseEntity<BiometriaResponse> cadastrarBiometria(@PathVariable("id") Long idCartao,
			@Valid @RequestBody BiometriaRequest bioRequest, UriComponentsBuilder uriBuilder) {

		Cartao cartao = em.find(Cartao.class, idCartao);

		if (cartao == null)
			return ResponseEntity.notFound().build();

		Biometria biometria = bioRequest.toModel(cartao);
		cartao.associarBiometria(biometria);
		em.persist(biometria);
		em.merge(cartao);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
				.buildAndExpand(biometria.getId()).toUri()).body(new BiometriaResponse(biometria));
	}

}
