package br.com.zupacademy.proposta.controller;

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

import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CarteiraDigital;
import br.com.zupacademy.proposta.models.request.CarteiraDigitalRequest;
import br.com.zupacademy.proposta.repository.CartaoRepository;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;
import br.com.zupacademy.proposta.utilsi.AvaliaAssociacaoComCarteiraDigital;

@RestController
@RequestMapping("/carteiras/cartao")
public class CarteiraDigitalController {

	private final CartaoRepository cartaoRepository;
	private final ExecutarTransacao executarTransacao;

	public CarteiraDigitalController(CartaoRepository cartaoRepository, ExecutarTransacao executarTransacao) {
		this.cartaoRepository = cartaoRepository;
		this.executarTransacao = executarTransacao;
	}

	@PostMapping("/{id}")
	@Transactional
	public ResponseEntity<?> cadastrarCarteiraDigital(@PathVariable("id") Long id, @Valid @RequestBody CarteiraDigitalRequest request) {

		Optional<Cartao> cartao = cartaoRepository.findById(id);

		return cartao.map(card -> {
			CarteiraDigital carteiraDigital = request.toModel(card);

			AvaliaAssociacaoComCarteiraDigital.validarAssociacao(card, carteiraDigital, executarTransacao);

			executarTransacao.salvarRegistro(carteiraDigital);

			return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
					.buildAndExpand(carteiraDigital.getId()).toUri()).build();

		}).orElse(ResponseEntity.notFound().build());

	}

}
