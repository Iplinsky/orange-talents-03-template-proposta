package br.com.zupacademy.proposta.models;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

@Entity
public class CartaoBloqueio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private Instant instanteDoBloqueio = Instant.now();

	@NotBlank
	@Column(nullable = false)
	private String ipClienteRequisicao;

	@NotBlank
	@Column(nullable = false)
	private String userAgentRequisicao;

	@Valid
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cartao_id", nullable = false)
	private Cartao cartao;

	@Deprecated
	public CartaoBloqueio() {
	}

	public CartaoBloqueio(@NotBlank String ipClienteRequisicao, @NotBlank String userAgentRequisicao,
			@Valid @NotNull Cartao cartao) {
		this.ipClienteRequisicao = ipClienteRequisicao;
		this.userAgentRequisicao = userAgentRequisicao;
		this.cartao = cartao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanteDoBloqueio == null) ? 0 : instanteDoBloqueio.hashCode());
		result = prime * result + ((ipClienteRequisicao == null) ? 0 : ipClienteRequisicao.hashCode());
		result = prime * result + ((userAgentRequisicao == null) ? 0 : userAgentRequisicao.hashCode());
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
		CartaoBloqueio other = (CartaoBloqueio) obj;
		if (instanteDoBloqueio == null) {
			if (other.instanteDoBloqueio != null)
				return false;
		} else if (!instanteDoBloqueio.equals(other.instanteDoBloqueio))
			return false;
		if (ipClienteRequisicao == null) {
			if (other.ipClienteRequisicao != null)
				return false;
		} else if (!ipClienteRequisicao.equals(other.ipClienteRequisicao))
			return false;
		if (userAgentRequisicao == null) {
			if (other.userAgentRequisicao != null)
				return false;
		} else if (!userAgentRequisicao.equals(other.userAgentRequisicao))
			return false;
		return true;
	}

}
