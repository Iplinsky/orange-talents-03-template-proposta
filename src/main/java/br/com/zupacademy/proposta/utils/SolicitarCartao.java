package br.com.zupacademy.proposta.utils;

import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.request.SolicitacaoCartaoRequest;

@Component
public class SolicitarCartao {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public SolicitarCartao(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		SolicitarCartao.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static void realizarSolicitacaoDeCartao(SolicitacaoCartaoRequest cartaoRequisicao) {
		comunicaComSetorDeCartoes.solicitarCartao(cartaoRequisicao);
	}

}
