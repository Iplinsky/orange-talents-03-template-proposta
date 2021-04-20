package br.com.zupacademy.proposta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zupacademy.proposta.models.request.CartaoComunicaBloqueioRequest;
import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;
import br.com.zupacademy.proposta.models.response.CartaoResponse;
import feign.Headers;

@FeignClient(url = "${analise.cartao}", name = "cartaoFeign")
public interface ComunicaComSetorDeCartoes {

	@PostMapping
	public void solicitarCartao(SolicitacaoCartaoRequest cartaoRequest);

	@GetMapping
	public CartaoResponse recuperarCartao(@RequestParam(required = true) String idProposta);

	@PostMapping("/{id}/bloqueios")	
	@Headers("Content-Type: application/json")
	public void comunicarBloqueioDoCartao(@PathVariable("id") String nrCartao, @RequestBody CartaoComunicaBloqueioRequest cartaoComunicaBloqueioRequest);

}
