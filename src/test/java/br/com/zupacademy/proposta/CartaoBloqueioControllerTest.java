package br.com.zupacademy.proposta;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CartaoBloqueioControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	URI uri;

	@BeforeEach
	void setUp() throws URISyntaxException {
		uri = new URI("http://localhost:8080/bloqueio-cartoes");
	}
	
	@Test
	@DisplayName("Deve bloquear o cartão com sucesso")
	void deveBloquearOCartaoComSucesso() throws Exception {		
		mockMvc.perform(
				 post(uri + "/1")
				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
		.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "http://localhost:8080/1"))
		.andDo(print());		
	}
	
	@Test
	@DisplayName("Não deve prosseguir com a solicitação de bloqueio caso o cartão já esteja bloqueado")
	void naoDeveProsseguirComASolicitacaoDeBloqueioCasoOCartaoJaEstejaBloqueado() throws Exception {
		mockMvc.perform(
				 post(uri + "/2")
				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andDo(print());		
	}
	
	@Test
	@DisplayName("Não deve prosseguir com a solicitação de bloqueio para um cartão inexistente")
	void naoDeveProsseguirComASolicitacaoDeBloqueioParaUmCartaoInexistente() throws Exception {
		mockMvc.perform(
				 post(uri + "/3")
				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
