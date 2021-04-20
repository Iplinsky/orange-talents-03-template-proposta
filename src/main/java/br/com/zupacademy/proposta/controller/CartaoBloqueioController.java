package br.com.zupacademy.proposta.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zupacademy.proposta.enums.StatusBloqueioCartao;
import br.com.zupacademy.proposta.handler.exception.ApiErroException;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoBloqueio;
import br.com.zupacademy.proposta.repository.CartaoRepository;
import br.com.zupacademy.proposta.utils.AvaliaBloqueioDoCartao;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;

@RestController
@RequestMapping("/bloqueio-cartoes")
public class CartaoBloqueioController {

	private static CartaoRepository cartaoRepository;
	private static ExecutarTransacao executarTransacao;

	public CartaoBloqueioController(CartaoRepository cartaoRepository, ExecutarTransacao executarTransacao) {
		CartaoBloqueioController.cartaoRepository = cartaoRepository;
		CartaoBloqueioController.executarTransacao = executarTransacao;
	}

	@PostMapping("/{id}")
	@Transactional
	public ResponseEntity<?> bloquearCartao(@PathVariable("id") Long id,
			@RequestHeader(name = "User-Agent") String userAgent, HttpServletRequest httpRequest) {

		Optional<Cartao> cartao = cartaoRepository.findById(id);

		return cartao.map(card -> {

			if (card.getStatusBloqueioCartao().equals(StatusBloqueioCartao.BLOQUEADO))
				throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "O cartão já se encontra bloqueado.");

			card = AvaliaBloqueioDoCartao.validaBloqueioDoCartao(card, executarTransacao);

			executarTransacao.salvarRegistro(new CartaoBloqueio(
					Optional.ofNullable(httpRequest.getHeader("X-FORWARDED-FOR")).orElse(httpRequest.getRemoteAddr()), userAgent, card));

			return ResponseEntity.ok().header("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/{id}").buildAndExpand(card.getId()).toUri().toString()).build();

		}).orElse(ResponseEntity.notFound().build());
	}

}
