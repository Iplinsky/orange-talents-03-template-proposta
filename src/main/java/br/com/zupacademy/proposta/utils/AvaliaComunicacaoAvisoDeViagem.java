package br.com.zupacademy.proposta.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CartaoAvisoViagem;
import br.com.zupacademy.proposta.models.request.CartaoComunicaAvisoDeViagemFeignRequest;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;

@Component
public class AvaliaComunicacaoAvisoDeViagem {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public AvaliaComunicacaoAvisoDeViagem(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		AvaliaComunicacaoAvisoDeViagem.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static void validaAvisoDeViagem(Cartao card, CartaoAvisoViagem cartaoAvisoViagem,
			ExecutarTransacao executarTransacao, Tracer tracer) {
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("cartao.id", card.getId());
		activeSpan.log(Map.of("event", "processo de validação iniciado para emitir um aviso de viagem para o cartão de id: " + card.getId()));		
		try {
			comunicaComSetorDeCartoes.avisarViagem(card.getNrCartao(), new CartaoComunicaAvisoDeViagemFeignRequest(
					cartaoAvisoViagem.getDestinoDaViagem(), cartaoAvisoViagem.getDataTerminoViagem()));

			card.atribuirAvisoDeViagem(cartaoAvisoViagem);
			executarTransacao.atualizarRegistro(card);
			activeSpan.log(Map.of("event", "processo de validação finalizado para emitir um aviso de viagem para o cartão de id: " + card.getId()));
		} catch (FeignException ex) {
			throw ex;
		}
	}
}
