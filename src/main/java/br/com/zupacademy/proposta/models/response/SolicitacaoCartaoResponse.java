package br.com.zupacademy.proposta.models.response;

import br.com.zupacademy.proposta.enums.TipoRestricao;

public class SolicitacaoCartaoResponse {

	private String documento;
	private String nome;
	private TipoRestricao resultadoSolicitacao;
	private String idProposta;

	public SolicitacaoCartaoResponse(String documento, String nome, String resultadoSolicitacao,
			String idProposta) {
		this.documento = documento;
		this.nome = nome;
		this.resultadoSolicitacao = TipoRestricao.valueOf(resultadoSolicitacao);
		this.idProposta = idProposta;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
	}

	public TipoRestricao getTipoRestricao() {
		return resultadoSolicitacao;
	}

	public String getIdProposta() {
		return idProposta;
	}

}
