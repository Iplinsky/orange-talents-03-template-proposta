package br.com.zupacademy.proposta;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.proposta.models.request.CarteiraDigitalRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CarteiraDigitalControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	URI uri;

	@BeforeEach
	void setUp() throws URISyntaxException {
		uri = new URI("http://localhost:8080/carteiras/cartao");
	}

	String toJson(CarteiraDigitalRequest carteiraDigitalRequest) throws JsonProcessingException {
		return mapper.writeValueAsString(carteiraDigitalRequest);
	}
	
//	@Test
//	@DisplayName("Deve associar o cartão com uma carteira digital com sucesso")
//	void deveAssociarOCartaoComUmaCarteiraDigitalComSucesso() throws Exception {
//		
//		mockMvc.perform(
//				  post(uri + "/1")
//				 .contentType(APPLICATION_JSON)
//				 .content(toJson(new CarteiraDigitalRequest("teste@gmail.com", TipoCarteiraDigital.PAYPAL.toString()))))
//		.andExpect(header().exists(LOCATION))
//		.andExpect(status().isCreated());
//		
//	}
	
	@Test
	@DisplayName("Não deve associar o cartão com uma carteira digital já associada anteriormente")
	void naoDeveAssociarOCartaoComUmaCarteiraDigitalJaAssociadaAnteriormente() throws Exception {
		
		mockMvc.perform(
				  post(uri + "/1")
				 .contentType(APPLICATION_JSON)
				 .content(toJson(new CarteiraDigitalRequest("teste@gmail.com", "PAYPAL"))))		
		.andExpect(status().isUnprocessableEntity());
		
	}

	@Test
	@DisplayName("Não deve associar o cartão a uma carteira digital inexistente")
	void naoDeveAssociarOCartaoAUmaCarteiraDigitalInexistente() throws Exception {

		assertThrows(IllegalArgumentException.class, () -> {
			mockMvc.perform(
					 post(uri + "/1")
					.contentType(APPLICATION_JSON)
					.content(toJson(new CarteiraDigitalRequest("teste@gmail.com", "PAY-PAL"))));
		});
	}
	
	@Test
	@DisplayName("Não deve associar o cartão a uma carteira digital se o email estiver em branco")
	void naoDeveAssociarOCartaoAUmaCarteiraSeOEMailEstiverEmBranco() throws Exception {

		mockMvc.perform(
				  post(uri + "/1")
				 .contentType(APPLICATION_JSON)
				 .content(toJson(new CarteiraDigitalRequest("", "PAYPAL"))))
		.andExpect(status().isBadRequest());		
	}
	
	@Test
	@DisplayName("Não deve associar o cartão a uma carteira digital se o email estiver em um formato inválido")
	void naoDeveAssociarOCartaoAUmaCarteiraSeOEMailEstiverEmFormatoInvalido() throws Exception {

		mockMvc.perform(
				  post(uri + "/1")
				 .contentType(APPLICATION_JSON)
				 .content(toJson(new CarteiraDigitalRequest("testegmail.com", "PAYPAL"))))
		.andExpect(status().isBadRequest());		
	}	
}
