package br.com.zupacademy.proposta.executa.transacao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

/*
 * Classe criada para controlar o escopo de transação do objeto Proposta
 * liberando o pool de conexões com o banco de dados devido a conexão com serviço externo
 */

@Component
public class ExecutarTransacaoProposta {

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

}
