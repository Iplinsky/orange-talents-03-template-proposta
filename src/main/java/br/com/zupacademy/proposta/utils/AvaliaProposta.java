package br.com.zupacademy.proposta.utils;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.enums.TipoRestricao;
import br.com.zupacademy.proposta.feign.VerificaRestricaoFinanceira;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;
import br.com.zupacademy.proposta.models.response.SolicitacaoCartaoResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;

@Component
public class AvaliaProposta {

	public static Proposta verificaSeExisteRestricaoFinanceira(Proposta proposta,
			VerificaRestricaoFinanceira verificaRestricaoFinanceira, Tracer tracer) {
		
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("proposta.id", proposta.getId());
		activeSpan.log(Map.of("event", "processo para verificar restrição financeira iniciado para a proposta de id: " + proposta.getId()));
		
		SolicitacaoCartaoRequest cartaoRequisicao = SolicitacaoCartaoRequest.build(proposta);

		try {
			SolicitacaoCartaoResponse retornoAnalise = verificaRestricaoFinanceira.verificaRestricao(cartaoRequisicao);

			Assert.notNull(retornoAnalise, "Houve uma falha no processo da análise de restrição financeira.");

			if (retornoAnalise.getTipoRestricao().equals(TipoRestricao.SEM_RESTRICAO)) {
				proposta.atribuirEstadoDaProposta(EstadoProposta.ELEGIVEL);
				/*
				 * Conforme o escopo do código, a proposta está elegível e portanto um cartão
				 * será solicitado através do método abaixo. A atribuição do cartão para a
				 * proposta será realizada através da funcionalidade Schedule da classe
				 * ScheduleVinculaCartao
				 */
				SolicitarCartao.realizarSolicitacaoDeCartao(cartaoRequisicao);
			}
			
			activeSpan.log(Map.of("event", "processo para verificar restrição financeira finalizado para a proposta de id: " + proposta.getId()));
			
		} catch (FeignException ex) {			
			if (ex.status() == 422) {				
				proposta = new Proposta(proposta, EstadoProposta.NAO_ELEGIVEL);
			}
		}

		return proposta;
	}

}
