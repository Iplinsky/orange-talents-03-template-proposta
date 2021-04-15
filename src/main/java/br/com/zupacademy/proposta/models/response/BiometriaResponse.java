package br.com.zupacademy.proposta.models.response;

import java.time.LocalDate;
import java.util.Base64;

import br.com.zupacademy.proposta.models.Biometria;

public class BiometriaResponse {

	private Long id;
	private LocalDate dataCriacaoBiometria;
	private String fingerPrint;

	public BiometriaResponse(Biometria biometria) {
		this.id = biometria.getId();
		this.dataCriacaoBiometria = biometria.getDataCriacaoBiometria();
		this.fingerPrint = new String(Base64.getDecoder().decode(biometria.getFingerPrint().getBytes()));
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDataCriacaoBiometria() {
		return dataCriacaoBiometria;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}
}
