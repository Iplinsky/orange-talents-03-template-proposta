package br.com.zupacademy.proposta.models.request;

import static org.springframework.util.Assert.notNull;

import java.util.Base64;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import br.com.zupacademy.proposta.models.Biometria;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.validators.UniqueValue;

public class BiometriaRequest {

	@NotBlank
	@UniqueValue(domain = Biometria.class, field = "fingerPrint")
	private String fingerPrint;

	public BiometriaRequest(@NotBlank String fingerPrint) {
		this.fingerPrint = Base64.getEncoder().encodeToString(fingerPrint.getBytes());
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public Biometria toModel(@Valid Cartao cartao) {
		notNull(cartao, "Cartão inválido");
		return new Biometria(cartao, this.fingerPrint);
	}
}
