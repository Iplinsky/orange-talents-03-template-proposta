package br.com.zupacademy.proposta.utilsi;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.zupacademy.proposta.feign.ComunicaComSetorDeCartoes;
import br.com.zupacademy.proposta.handler.exception.ApiErroException;
import br.com.zupacademy.proposta.models.Cartao;
import br.com.zupacademy.proposta.models.CarteiraDigital;
import br.com.zupacademy.proposta.models.request.CartaoAssociaCarteiraDigitalRequest;
import br.com.zupacademy.proposta.utils.ExecutarTransacao;
import feign.FeignException.FeignClientException;

@Component
public class AvaliaAssociacaoComCarteiraDigital {

	private static ComunicaComSetorDeCartoes comunicaComSetorDeCartoes;

	public AvaliaAssociacaoComCarteiraDigital(ComunicaComSetorDeCartoes comunicaComSetorDeCartoes) {
		AvaliaAssociacaoComCarteiraDigital.comunicaComSetorDeCartoes = comunicaComSetorDeCartoes;
	}

	public static void validarAssociacao(Cartao card, CarteiraDigital carteiraDigital,
			ExecutarTransacao executarTransacao) {

		card.getCarteiraDigital().stream()
				.filter(c -> c.getTipoCarteiraDigital().equals(carteiraDigital.getTipoCarteiraDigital()))
				.forEach(ex -> {
					throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY,
							"O cartão já está associado a esta carteira digital!");
				});

		try {
			comunicaComSetorDeCartoes.associarCarteiraDigital(card.getNrCartao(),
					new CartaoAssociaCarteiraDigitalRequest(carteiraDigital.getEmail(),
							carteiraDigital.getTipoCarteiraDigital()));

			card.associarCarteiraDigital(carteiraDigital);
			executarTransacao.atualizarRegistro(card);

		} catch (FeignClientException ex) {
			throw ex;
		}
	}
}