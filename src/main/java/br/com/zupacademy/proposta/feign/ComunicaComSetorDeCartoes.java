package br.com.zupacademy.proposta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;
import br.com.zupacademy.proposta.models.response.CartaoResponse;

@FeignClient(url = "${analise.cartao}", name = "recuperaCartao")
public interface ComunicaComSetorDeCartoes {

	@PostMapping
	public void solicitarCartao(SolicitacaoCartaoRequest cartaoRequest);

	@GetMapping
	public CartaoResponse recuperarCartao(@RequestParam(required = true) String idProposta);

}
