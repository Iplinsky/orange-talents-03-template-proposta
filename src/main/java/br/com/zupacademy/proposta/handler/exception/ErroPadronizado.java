package br.com.zupacademy.proposta.handler.exception;

import java.util.Collection;

public class ErroPadronizado {

	private Collection<String> mensagens;

	public ErroPadronizado(Collection<String> mensagens) {
		this.mensagens = mensagens;
	}

	public Collection<String> getMensagens() {
		return mensagens;
	}
}
