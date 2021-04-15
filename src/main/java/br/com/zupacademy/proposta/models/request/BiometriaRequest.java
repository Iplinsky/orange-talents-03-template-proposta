package br.com.zupacademy.proposta.models.request;

import static org.springframework.util.Assert.notNull;

import java.util.Base64;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zupacademy.proposta.models.Biometria;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.validators.UniqueValue;

public class BiometriaRequest {

	@NotBlank
	@UniqueValue(domain = Biometria.class, field = "fingerPrint")
	private String fingerPrint;

	@JsonCreator
	public BiometriaRequest(@NotBlank @JsonProperty("fingerPrint") String fingerPrint) {
		this.fingerPrint = Base64.getEncoder().encodeToString(fingerPrint.getBytes());
	}

	public Biometria toModel(@Valid Cartao cartao) {
		notNull(cartao, "Cartão inválido");
		return new Biometria(cartao, this.fingerPrint);
	}
}
