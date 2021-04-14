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
		return this.id;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNome() {
		return nome;
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
		result = prime * result + ((getDocumento() == null) ? 0 : getDocumento().hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((getNome() == null) ? 0 : getNome().hashCode());
		result = prime * result + ((salario == null) ? 0 : salario.hashCode());
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
		if (getDocumento() == null) {
			if (other.getDocumento() != null)
				return false;
		} else if (!getDocumento().equals(other.getDocumento()))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (getNome() == null) {
			if (other.getNome() != null)
				return false;
		} else if (!getNome().equals(other.getNome()))
			return false;
		if (salario == null) {
			if (other.salario != null)
				return false;
		} else if (!salario.equals(other.salario))
			return false;
		return true;
	}

}
