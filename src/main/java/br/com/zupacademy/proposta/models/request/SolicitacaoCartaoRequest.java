package br.com.zupacademy.proposta.models.request;

import br.com.zupacademy.proposta.models.Proposta;

public class SolicitacaoCartaoRequest {

	private String documento;
	private String nome;
	private String idProposta;

	public SolicitacaoCartaoRequest(String documento, String nome, String idProposta) {
		this.documento = documento;
		this.nome = nome;
		this.idProposta = idProposta;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public String getIdProposta() {
		return idProposta;
	}

	public static SolicitacaoCartaoRequest build(Proposta proposta) {
		return new SolicitacaoCartaoRequest(proposta.getDocumentoDecrypt(), proposta.getNome(), proposta.getId().toString());
	}

}
