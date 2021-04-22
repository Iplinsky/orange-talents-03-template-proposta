package br.com.zupacademy.proposta.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import br.com.zupacademy.proposta.enums.TipoCarteiraDigital;

@Entity
public class CarteiraDigital {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	@NotBlank
	@Column(nullable = false)
	private String email;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoCarteiraDigital tipoCarteiraDigital;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	@JoinColumn(name = "cartao_id", nullable = false)
	private Cartao cartao;

	@Deprecated
	public CarteiraDigital() {
	}

	public CarteiraDigital(@Email @NotBlank String email, TipoCarteiraDigital tipoCarteiraDigital,
			@NotNull @Valid Cartao cartao) {
		this.email = email;
		this.tipoCarteiraDigital = tipoCarteiraDigital;
		this.cartao = cartao;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public TipoCarteiraDigital getTipoCarteiraDigital() {
		return tipoCarteiraDigital;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoCarteiraDigital == null) ? 0 : tipoCarteiraDigital.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		CarteiraDigital other = (CarteiraDigital) obj;
		if (tipoCarteiraDigital != other.tipoCarteiraDigital)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

}
