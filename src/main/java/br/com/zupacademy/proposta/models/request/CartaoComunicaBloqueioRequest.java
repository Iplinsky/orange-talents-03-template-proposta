package br.com.zupacademy.proposta.models.request;

public class CartaoComunicaBloqueioRequest {

	private String sistemaResponsavel;

	public CartaoComunicaBloqueioRequest(String sistemaResponsavel) {
		this.sistemaResponsavel = sistemaResponsavel;
	}

	public String getSistemaResponsavel() {
		return sistemaResponsavel;
	}

}
