package br.com.zupacademy.proposta.models.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.crypto.encrypt.Encryptors;

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

	public Proposta toModel() {
		String encryptedDoc = Encryptors.text("${secret.key}", "cec1745a1b0d9d79").encrypt(this.documento);
		return new Proposta(encryptedDoc, this.email, this.nome, this.endereco, this.salario);
	}

}