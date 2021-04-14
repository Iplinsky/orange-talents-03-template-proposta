package br.com.zupacademy.proposta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zupacademy.proposta.models.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
