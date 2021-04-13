package br.com.zupacademy.proposta.utils;

import org.springframework.util.Assert;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.enums.TipoRestricao;
import br.com.zupacademy.proposta.feign.VerificaRestricaoFinanceira;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.SolicitacaoAnaliseRestricaoRequest;
import br.com.zupacademy.proposta.models.response.SolicitacaoAnaliseRestricaoResponse;
import feign.FeignException;

public class AvaliaProposta {

	public static Proposta verificaSeExisteRestricaoFinanceira(Proposta proposta,
			VerificaRestricaoFinanceira verificaRestricaoFinanceira) {

		try {
			SolicitacaoAnaliseRestricaoResponse retornoAnalise = verificaRestricaoFinanceira
					.verificaRestricao(SolicitacaoAnaliseRestricaoRequest.build(proposta));

			Assert.notNull(retornoAnalise, "Houve uma falha no processo da análise de restrição financeira.");
			if (retornoAnalise.getTipoRestricao().equals(TipoRestricao.SEM_RESTRICAO)) {
				proposta.atribuirEstadoDaProposta(EstadoProposta.ELEGIVEL);
			}

		} catch (FeignException ex) {
			if (ex.status() == 422) {
				proposta = new Proposta(proposta, EstadoProposta.NAO_ELEGIVEL);
			}
		}

		return proposta;
	}

}
