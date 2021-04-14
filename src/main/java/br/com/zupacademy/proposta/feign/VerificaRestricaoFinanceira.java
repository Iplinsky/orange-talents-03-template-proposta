package br.com.zupacademy.proposta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;
import br.com.zupacademy.proposta.models.response.SolicitacaoCartaoResponse;

@FeignClient(url = "${analise.financeira}", name = "verificaRestricao")
public interface VerificaRestricaoFinanceira {

	@PostMapping
	public SolicitacaoCartaoResponse verificaRestricao(SolicitacaoCartaoRequest request);

}
