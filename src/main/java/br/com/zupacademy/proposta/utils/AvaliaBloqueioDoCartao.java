package br.com.zupacademy.proposta.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.request.CartaoComunicaBloqueioRequest;
import feign.FeignException.FeignClientException;
import io.opentracing.Span;
import io.opentracing.Tracer;

@Component
public class AvaliaBloqueioDoCartao {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public AvaliaBloqueioDoCartao(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		AvaliaBloqueioDoCartao.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static Cartao validaBloqueioDoCartao(Cartao cartao, ExecutarTransacao executarTransacao, Tracer tracer) {
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("cartao.id", cartao.getId());
		activeSpan.log(Map.of("event", "processo para validar o bloqueio do cartão de id " + cartao.getId() + " iniciado."));
		try {
			comunicaComSetorDeCartoes.comunicarBloqueioDoCartao(cartao.getNrCartao(), new CartaoComunicaBloqueioRequest("proposta"));
			cartao.bloquearCartao();
			executarTransacao.atualizarRegistro(cartao);
			activeSpan.log(Map.of("event", "processo de validação finalizado para o bloqueio do cartão de id " + cartao.getId() + " finalizado com sucesso."));
		} catch (FeignClientException ex) {
			throw ex;
		}
		return cartao;
	}

}
