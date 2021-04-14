package br.com.zupacademy.proposta.models.response;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.models.Proposta;

public class PropostaResponse {

	private Long id;
	private String documento;
	private String email;
	private String nome;
	private String endereco;
	private Double salario;
	private EstadoProposta estadoDaProposta;

	public PropostaResponse(Proposta proposta) {
		this.id = proposta.getId();
		this.documento = proposta.getDocumento();
		this.email = proposta.getEmail();
		this.nome = proposta.getNome();
		this.endereco = proposta.getEndereco();
		this.salario = proposta.getSalario();
		this.estadoDaProposta = proposta.getEstadoDaProposta();
	}

	public Long getId() {
		return id;
	}

	public String getDocumento() {
		return documento;
	}

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public Double getSalario() {
		return salario;
	}

	public EstadoProposta getEstadoDaProposta() {
		return estadoDaProposta;
	}

}
