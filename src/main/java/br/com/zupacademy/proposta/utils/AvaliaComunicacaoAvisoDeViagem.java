package br.com.zupacademy.proposta.utils;

import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoAvisoViagem;
import br.com.zupacademy.proposta.models.request.CartaoComunicaAvisoDeViagemFeignRequest;
import feign.FeignException;

@Component
public class AvaliaComunicacaoAvisoDeViagem {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public AvaliaComunicacaoAvisoDeViagem(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		AvaliaComunicacaoAvisoDeViagem.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static void validaAvisoDeViagem(Cartao card, CartaoAvisoViagem cartaoAvisoViagem,
			ExecutarTransacao executarTransacao) {
		try {
			comunicaComSetorDeCartoes.avisarViagem(card.getNrCartao(), new CartaoComunicaAvisoDeViagemFeignRequest(
					cartaoAvisoViagem.getDestinoDaViagem(), cartaoAvisoViagem.getDataTerminoViagem()));

			card.atribuirAvisoDeViagem(cartaoAvisoViagem);
			executarTransacao.atualizarRegistro(card);
		} catch (FeignException ex) {
			throw ex;
		}
	}
}
