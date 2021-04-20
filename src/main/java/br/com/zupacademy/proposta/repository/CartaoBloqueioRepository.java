package br.com.zupacademy.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zupacademy.proposta.models.CartaoBloqueio;

public interface CartaoBloqueioRepository extends JpaRepository<CartaoBloqueio, Long> {

}