package br.com.zupacademy.proposta.models;

import static org.springframework.util.Assert.notNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.annotations.CreationTimestamp;

import br.com.zupacademy.proposta.enums.StatusBloqueioCartao;

@Entity
public class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(nullable = false)
	private String nrCartao;

	@CreationTimestamp
	@NotNull
	@Column(nullable = false)
	private LocalDateTime emitidoEm;

	@NotEmpty
	@Column(nullable = false)
	private String titular;

	@NotNull
	@Column(nullable = false)
	private Double limite;

	@Valid
	@OneToOne
	@JoinColumn(name = "proposta_id")
	private Proposta proposta;

	@Valid
	@OneToMany(mappedBy = "cartao")
	private Set<Biometria> biometria = new HashSet<Biometria>();

	@Valid
	@OneToMany(mappedBy = "cartao")
	private Set<CartaoBloqueio> bloqueiosDoCartao = new HashSet<CartaoBloqueio>();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusBloqueioCartao statusBloqueioCartao = StatusBloqueioCartao.DESBLOQUEADO;

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

	public Long getId() {
		return id;
	}

	public StatusBloqueioCartao getStatusBloqueioCartao() {
		return statusBloqueioCartao;
	}

	public void atribuirProposta(@Valid Proposta proposta) {
		notNull(proposta, "Proposta inválida.");
		this.proposta = proposta;
	}

	public void bloquearCartao() {
		this.statusBloqueioCartao = StatusBloqueioCartao.BLOQUEADO;
	}

	public void associarBiometria(@Valid Biometria biometria) {
		notNull(biometria, "Biometria inválida.");
		this.biometria.add(biometria);
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
