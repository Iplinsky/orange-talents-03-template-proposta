package br.com.zupacademy.proposta.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.response.CartaoResponse;
import br.com.zupacademy.proposta.repository.PropostaRepository;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;

@Component
public class ScheduleVinculaCartao {

	private PropostaRepository propostaRepository;
	private ExecutarTransacao executarTransacao;
	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public ScheduleVinculaCartao(PropostaRepository propostaRepository, ExecutarTransacao executarTransacao,
			ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		this.propostaRepository = propostaRepository;
		this.executarTransacao = executarTransacao;
		ScheduleVinculaCartao.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	@Scheduled(fixedRateString = "${periodicidade.consulta-cartao}")
	public void vinculaCartaoNaProposta() {
		CartaoResponse cartaoRetornado = new CartaoResponse();

		List<Proposta> listaDePropostasElegiveis = propostaRepository
				.findAllByEstadoDaProposta(EstadoProposta.ELEGIVEL);

		for (Proposta proposta : listaDePropostasElegiveis) {
			try {
				cartaoRetornado = comunicaComSetorDeCartoes.recuperarCartao(proposta.getId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Cartao cartao = cartaoRetornado.toModel();
			executarTransacao.salvarRegistro(cartao);
			/*
			 * Período que realiza o vinculo do cartão com a proposta encaminhada. Uma nova
			 * categoria de estado da proposta foi criada para evitar que ela entre dentro
			 * deste loop sem a real necessidade.
			 */
			proposta.vincularCartao(cartao);
			proposta.atribuirEstadoDaProposta(EstadoProposta.CONCLUIDA);
			cartao.atribuirProposta(proposta);
			executarTransacao.atualizarRegistro(proposta);
		}
	}

}
