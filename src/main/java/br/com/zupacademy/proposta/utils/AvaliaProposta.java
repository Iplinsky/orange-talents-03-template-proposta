package br.com.zupacademy.proposta.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.enums.TipoRestricao;
import br.com.zupacademy.proposta.feign.VerificaRestricaoFinanceira;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;
import br.com.zupacademy.proposta.models.response.SolicitacaoCartaoResponse;
import feign.FeignException;

@Component
public class AvaliaProposta {

	public static Proposta verificaSeExisteRestricaoFinanceira(Proposta proposta,
			VerificaRestricaoFinanceira verificaRestricaoFinanceira) {

		SolicitacaoCartaoRequest cartaoRequisicao = SolicitacaoCartaoRequest.build(proposta);

		try {
			SolicitacaoCartaoResponse retornoAnalise = verificaRestricaoFinanceira.verificaRestricao(cartaoRequisicao);

			Assert.notNull(retornoAnalise, "Houve uma falha no processo da análise de restrição financeira.");

			if (retornoAnalise.getTipoRestricao().equals(TipoRestricao.SEM_RESTRICAO)) {
				proposta.atribuirEstadoDaProposta(EstadoProposta.ELEGIVEL);
				/*
				 * Conforme o escopo do código, a proposta está elegível e portanto um cartão será
				 * solicitado através do método abaixo. 
				 * A atribuição do cartão para a proposta será realizada através da funcionalidade Schedule da classe ScheduleVinculaCartao
				 */
				SolicitarCartao.realizarSolicitacaoDeCartao(cartaoRequisicao);
			}

		} catch (FeignException ex) {
			if (ex.status() == 422) {
				proposta = new Proposta(proposta, EstadoProposta.NAO_ELEGIVEL);
			}
		}

		return proposta;
	}

}
