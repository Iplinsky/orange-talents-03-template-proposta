package br.com.zupacademy.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zupacademy.proposta.models.Proposta;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

	boolean existsPropostaByDocumento(String documento);

}
