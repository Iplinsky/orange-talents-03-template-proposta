package br.com.zupacademy.proposta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.models.Proposta;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

	boolean existsPropostaByDocumento(String documento);

	List<Proposta> findAllByEstadoDaProposta(EstadoProposta elegivel);

	Proposta findByEmail(String email);

}
