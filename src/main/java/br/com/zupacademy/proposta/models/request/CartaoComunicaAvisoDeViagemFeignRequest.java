package br.com.zupacademy.proposta.models.request;

import java.time.LocalDate;

public class CartaoComunicaAvisoDeViagemFeignRequest {

	private String destino;
	private LocalDate validoAte;

	public CartaoComunicaAvisoDeViagemFeignRequest(String destino, LocalDate validoAte) {
		this.destino = destino;
		this.validoAte = validoAte;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDate getValidoAte() {
		return validoAte;
	}

}