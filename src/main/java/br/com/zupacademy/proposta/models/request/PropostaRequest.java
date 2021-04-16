package br.com.zupacademy.proposta.models.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.validators.ValidateCpfCnpj;

public class PropostaRequest {

	@ValidateCpfCnpj
	@NotBlank
	private String documento;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String nome;
	@NotBlank
	private String endereco;
	@NotNull
	@Positive
	private Double salario;

	public PropostaRequest(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
			@NotBlank String endereco, @NotNull @Positive Double salario) {
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Proposta toModel() {
		return new Proposta(this.documento, this.email, this.nome, this.endereco, this.salario);
	}

	/*
	 * Utilizado para análise na classe PropostaControllerTest 
	 * @Test void  deveCriarUmaNovaProposta()
	 */
	@Override
	public String toString() {
		return "PropostaRequest [documento=" + documento + ", email=" + email + ", nome=" + nome + ", endereco="
				+ endereco + ", salario=" + salario + "]";
	}

}