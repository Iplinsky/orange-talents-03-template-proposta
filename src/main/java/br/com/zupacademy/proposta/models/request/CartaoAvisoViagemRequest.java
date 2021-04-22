package br.com.zupacademy.proposta.models.request;

import static org.springframework.util.Assert.notNull;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoAvisoViagem;

public class CartaoAvisoViagemRequest {

	@NotBlank
	private String destinoDaViagem;

	@Future
	@NotNull
	private LocalDate dataTerminoViagem;

	public CartaoAvisoViagemRequest(@NotBlank String destinoDaViagem, @Future @NotNull LocalDate dataTerminoViagem) {
		this.destinoDaViagem = destinoDaViagem;
		this.dataTerminoViagem = dataTerminoViagem;
	}

	public CartaoAvisoViagem toModel(String ipClient, String userAgent, @Valid Cartao card) {
		notNull(card, "Cartão inválido!");
		return new CartaoAvisoViagem(userAgent, dataTerminoViagem, ipClient, userAgent, card);
	}
}
