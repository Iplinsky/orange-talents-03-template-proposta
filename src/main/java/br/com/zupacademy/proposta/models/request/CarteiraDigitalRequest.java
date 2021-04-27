package br.com.zupacademy.proposta.models.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import br.com.zupacademy.proposta.enums.TipoCarteiraDigital;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CarteiraDigital;

public class CarteiraDigitalRequest {

	@Email
	@NotBlank
	private String email;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoCarteiraDigital tipoCarteiraDigital;

	public CarteiraDigitalRequest(@Email @NotBlank String email, @NotBlank String tipoCarteiraDigital) {
		this.email = email;
		this.tipoCarteiraDigital = TipoCarteiraDigital.valueOf(tipoCarteiraDigital);
	}

	public String getEmail() {
		return email;
	}

	public TipoCarteiraDigital getTipoCarteiraDigital() {
		return tipoCarteiraDigital;
	}

	public CarteiraDigital toModel(Cartao card) {
		return new CarteiraDigital(email, tipoCarteiraDigital, card);
	}

}
