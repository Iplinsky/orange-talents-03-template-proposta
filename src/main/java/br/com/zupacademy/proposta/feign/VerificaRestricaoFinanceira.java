package br.com.zupacademy.proposta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zupacademy.proposta.models.request.SolicitacaoAnaliseRestricaoRequest;
import br.com.zupacademy.proposta.models.response.SolicitacaoAnaliseRestricaoResponse;

@FeignClient(url = "http://localhost:9999/api/solicitacao", name = "verificaRestricao")
public interface VerificaRestricaoFinanceira {

	@PostMapping
	public SolicitacaoAnaliseRestricaoResponse verificaRestricao(SolicitacaoAnaliseRestricaoRequest request);

}
