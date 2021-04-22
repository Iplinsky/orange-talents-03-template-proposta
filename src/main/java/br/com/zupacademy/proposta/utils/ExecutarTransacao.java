package br.com.zupacademy.proposta.utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class ExecutarTransacao {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public <T> T salvarRegistro(T object) {
		entityManager.persist(object);
		return object;
	}

	@Transactional
	public <T> T atualizarRegistro(T object) {
		entityManager.merge(object);
		return object;
	}

	public <T> Object buscarRegistroPeloId(T Object, Long id) {
		return entityManager.find(Object.getClass(), id);
	}

}
