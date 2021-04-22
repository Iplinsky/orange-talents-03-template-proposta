package br.com.zupacademy.proposta.utils;

import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.request.CartaoComunicaBloqueioRequest;
import feign.FeignException.FeignClientException;

@Component
public class AvaliaBloqueioDoCartao {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public AvaliaBloqueioDoCartao(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		AvaliaBloqueioDoCartao.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static Cartao validaBloqueioDoCartao(Cartao cartao, ExecutarTransacao executarTransacao) {
		try {
			comunicaComSetorDeCartoes.comunicarBloqueioDoCartao(cartao.getNrCartao(), new CartaoComunicaBloqueioRequest("proposta"));
			cartao.bloquearCartao();
			executarTransacao.atualizarRegistro(cartao);
		} catch (FeignClientException ex) {
			throw ex;
		}
		return cartao;
	}

}
