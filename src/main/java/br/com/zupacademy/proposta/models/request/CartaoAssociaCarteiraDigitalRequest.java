package br.com.zupacademy.proposta.models.request;

import br.com.zupacademy.proposta.enums.TipoCarteiraDigital;

public class CartaoAssociaCarteiraDigitalRequest {

	private String email;
	private String carteira;

	public CartaoAssociaCarteiraDigitalRequest(String email, TipoCarteiraDigital tipoCarteiraDigital) {
		this.email = email;
		this.carteira = tipoCarteiraDigital.toString();
	}

	public String getEmail() {
		return email;
	}

	public String getCarteira() {
		return carteira;
	}

}
