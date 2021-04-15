package br.com.zupacademy.proposta.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Biometria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@CreationTimestamp
	private LocalDate dataCriacaoBiometria = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "cartao_id")
	@Valid
	@NotNull
	private Cartao cartao;

	@Lob
	@Column(nullable = false, unique = true)
	private String fingerPrint;

	@Deprecated
	public Biometria() {
	}

	public Biometria(@Valid @NotNull Cartao cartao, @NotBlank String fingerPrint) {
		this.cartao = cartao;
		this.fingerPrint = fingerPrint;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDataCriacaoBiometria() {
		return dataCriacaoBiometria;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fingerPrint == null) ? 0 : fingerPrint.hashCode());
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
		Biometria other = (Biometria) obj;
		if (fingerPrint == null) {
			if (other.fingerPrint != null)
				return false;
		} else if (!fingerPrint.equals(other.fingerPrint))
			return false;
		return true;
	}

}
