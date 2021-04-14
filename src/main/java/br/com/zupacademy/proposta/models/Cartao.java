package br.com.zupacademy.proposta.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(nullable = false)
	private String nrCartao;

	@CreationTimestamp
	@PastOrPresent
	@NotNull
	@Column(nullable = false)
	private LocalDateTime emitidoEm;

	@NotEmpty
	@Column(nullable = false)
	private String titular;

	@NotNull
	@Column(nullable = false)
	private Double limite;

	@OneToOne
	@JoinColumn(name = "proposta_id")
	private Proposta proposta;

	@Deprecated
	public Cartao() {
	}

	public Cartao(@NotEmpty String nrCartao, @PastOrPresent @NotNull LocalDateTime emitidoEm, @NotEmpty String titular,
			@NotNull Double limite) {
		super();
		this.nrCartao = nrCartao;
		this.emitidoEm = emitidoEm;
		this.titular = titular;
		this.limite = limite;
	}

	public void atribuirProposta(@Valid Proposta proposta) {
		this.proposta = proposta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nrCartao == null) ? 0 : nrCartao.hashCode());
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
		Cartao other = (Cartao) obj;
		if (nrCartao == null) {
			if (other.nrCartao != null)
				return false;
		} else if (!nrCartao.equals(other.nrCartao))
			return false;
		return true;
	}

}
