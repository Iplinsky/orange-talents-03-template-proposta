package br.com.zupacademy.proposta.models.response;

import java.time.LocalDateTime;

import br.com.zupacademy.proposta.models.Cartao;

public class CartaoResponse {

	private String id;
	private LocalDateTime emitidoEm;
	private String titular;
	private Double limite;
	private String idProposta;

	public CartaoResponse() {
	}

	public CartaoResponse(String id, LocalDateTime emitidoEm, String titular, Double limite, String idProposta) {
		this.id = id;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
		this.idProposta = idProposta;
	}

	public String getId() {
		return id;
	}

	public LocalDateTime getEmitidoEm() {
		return emitidoEm;
	}

	public String getTitular() {
		return titular;
	}

	public Double getLimite() {
		return limite;
	}

	public String getIdProposta() {
		return idProposta;
	}

	public Cartao toModel() {
		return new Cartao(id, emitidoEm, titular, limite);
	}

}
