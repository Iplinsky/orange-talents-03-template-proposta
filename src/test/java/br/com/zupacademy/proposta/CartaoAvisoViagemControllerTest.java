package br.com.zupacademy.proposta;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.proposta.models.request.CartaoAvisoViagemRequest;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CartaoAvisoViagemControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	URI uri;

	@BeforeEach
	void setUp() throws URISyntaxException {
		uri = new URI("http://localhost:8080/cartao");		
	}
	
	String convertToJson(CartaoAvisoViagemRequest cartaoAvisoViagemRequest) throws JsonProcessingException {
		return mapper.writeValueAsString(cartaoAvisoViagemRequest);
	}
		
	@Test
	@DisplayName("Deve cadastrar o aviso de viagem com sucesso")
	void deveCadastrarOAvisoDeViagemComSucesso() throws Exception {				
		mockMvc.perform(
				 post(uri + "/1/aviso-viagem")
				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new CartaoAvisoViagemRequest("Londres", LocalDate.now().plusMonths(1)))))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
		.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "http://localhost:8080/1"))		
		.andDo(print());
	}
	

//	@Test
//	@DisplayName("Não deve cadastrar um aviso de viagem idêntico")
//	void naoDeveCadastrarUmAvisoDeViagemIdentico() throws Exception {
//		mockMvc.perform(
//				 post(uri + "/1/aviso-viagem")
//				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(convertToJson(new CartaoAvisoViagemRequest("Londres", LocalDate.now().plusMonths(1)))))
//		.andExpect(status().isUnprocessableEntity())
//		.andDo(print());
//	}
	
	@Test
	@DisplayName("Não deve cadastrar o aviso de viagem para um cartão inexistente")
	void naoDeveCadastrarOAvisoDeViagemParaUmCartaoInexistente() throws Exception {
		mockMvc.perform(
				 post(uri + "/3/aviso-viagem")
				.header(HttpHeaders.USER_AGENT, "Mock-Agent")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
}
