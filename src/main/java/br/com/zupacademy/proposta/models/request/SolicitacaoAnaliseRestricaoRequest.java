package br.com.zupacademy.proposta.models.request;

import br.com.zupacademy.proposta.models.Proposta;

public class SolicitacaoAnaliseRestricaoRequest {

	private String documento;
	private String nome;
	private String idProposta;

	public SolicitacaoAnaliseRestricaoRequest(String documento, String nome, String idProposta) {
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

	public static SolicitacaoAnaliseRestricaoRequest build(Proposta proposta) {
		return new SolicitacaoAnaliseRestricaoRequest(proposta.getDocumento(), proposta.getNome(),
				proposta.getId().toString());
	}

}
