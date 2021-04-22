package br.com.zupacademy.proposta.models;

import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.CreationTimestamp;

import com.sun.istack.NotNull;

@Entity
public class CartaoAvisoViagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String destinoDaViagem;

	@Future
	@NotNull
	@Column(nullable = false)
	private LocalDate dataTerminoViagem;

	@NotNull
	@PastOrPresent
	@CreationTimestamp
	@Column(nullable = false)
	private Instant instanteAvisoViagem = Instant.now();

	@NotBlank
	@Column(nullable = false)
	private String ipClient;

	@NotBlank
	@Column(nullable = false)
	private String userAgent;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "cartao_id", nullable = false)
	private Cartao cartao;

	@Deprecated
	public CartaoAvisoViagem() {
	}

	public CartaoAvisoViagem(@NotBlank String destinoDaViagem, @Future @NotNull LocalDate dataTerminoViagem,
			@NotBlank String ipClient, @NotBlank String userAgent, @NotNull Cartao cartao) {
		this.destinoDaViagem = destinoDaViagem;
		this.dataTerminoViagem = dataTerminoViagem;
		this.ipClient = ipClient;
		this.userAgent = userAgent;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartao == null) ? 0 : cartao.hashCode());
		result = prime * result + ((dataTerminoViagem == null) ? 0 : dataTerminoViagem.hashCode());
		result = prime * result + ((destinoDaViagem == null) ? 0 : destinoDaViagem.hashCode());
		result = prime * result + ((instanteAvisoViagem == null) ? 0 : instanteAvisoViagem.hashCode());
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
		CartaoAvisoViagem other = (CartaoAvisoViagem) obj;
		if (cartao == null) {
			if (other.cartao != null)
				return false;
		} else if (!cartao.equals(other.cartao))
			return false;
		if (dataTerminoViagem == null) {
			if (other.dataTerminoViagem != null)
				return false;
		} else if (!dataTerminoViagem.equals(other.dataTerminoViagem))
			return false;
		if (destinoDaViagem == null) {
			if (other.destinoDaViagem != null)
				return false;
		} else if (!destinoDaViagem.equals(other.destinoDaViagem))
			return false;
		if (instanteAvisoViagem == null) {
			if (other.instanteAvisoViagem != null)
				return false;
		} else if (!instanteAvisoViagem.equals(other.instanteAvisoViagem))
			return false;
		return true;
	}
}
