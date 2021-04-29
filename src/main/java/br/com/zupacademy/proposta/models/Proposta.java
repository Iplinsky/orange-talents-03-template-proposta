package br.com.zupacademy.proposta.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.crypto.encrypt.Encryptors;

import br.com.zupacademy.proposta.enums.EstadoProposta;

@Entity
public class Proposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String documento;

	@Email
	@NotBlank
	@Column(nullable = false)
	private String email;

	@NotBlank
	@Column(nullable = false)
	private String nome;

	@NotBlank
	@Column(nullable = false)
	private String endereco;

	@NotNull
	@Positive
	@Column(nullable = false)
	private Double salario;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false)
	private EstadoProposta estadoDaProposta = EstadoProposta.NAO_ELEGIVEL;

	@OneToOne(mappedBy = "proposta")
	private Cartao cartao;

	@Deprecated
	public Proposta() {
	}

	public Proposta(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
			@NotBlank String endereco, @NotNull @Positive Double salario) {
		this.documento = documento;
		this.email = email;
		this.nome = nome;
		this.endereco = endereco;
		this.salario = salario;
	}

	public Proposta(@Valid Proposta proposta, @NotNull EstadoProposta estadoDaProposta) {
		this.id = proposta.id;
		this.documento = proposta.documento;
		this.email = proposta.email;
		this.nome = proposta.nome;
		this.endereco = proposta.endereco;
		this.salario = proposta.salario;
		this.estadoDaProposta = estadoDaProposta;
	}

	public Long getId() {
		return id;
	}

	public String getDocumento() {
		return documento;
	}

	public String getDocumentoDecrypt() {
		return Encryptors.text("${secret.key}", "cec1745a1b0d9d79").decrypt(documento);
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

	public Cartao getCartao() {
		return cartao;
	}

	public EstadoProposta getEstadoDaProposta() {
		return estadoDaProposta;
	}

	public void atribuirEstadoDaProposta(@NotNull EstadoProposta estadoDaProposta) {
		this.estadoDaProposta = estadoDaProposta;
	}

	public void vincularCartao(@Valid Cartao cartao) {
		this.cartao = cartao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposta other = (Proposta) obj;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		return true;
	}

}
