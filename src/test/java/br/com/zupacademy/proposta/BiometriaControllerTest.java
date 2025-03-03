package br.com.zupacademy.proposta;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.proposta.models.request.BiometriaRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class BiometriaControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	URI uri;

	@BeforeEach
	void setUp() throws URISyntaxException {
		uri = new URI("http://localhost:8080/biometrias/cartao");
	}
	
	String convertToJson(BiometriaRequest biometriaRequest) throws JsonProcessingException{
		return mapper.writeValueAsString(biometriaRequest);		
	}

	@Test
	@DisplayName("Deve cadastrar uma novo biometria para o cartão com sucesso")
	void deveCadastrarUmaNovaBiometriaParaOCartaoComSucesso() throws JsonProcessingException, Exception {		
		
		mockMvc.perform(
				post(uri + "/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new BiometriaRequest("fingerPrint"))))
		.andExpect(status().isCreated())
		.andExpect(header().exists(HttpHeaders.LOCATION))
		.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/1"))
		.andDo(print());
	}
	
	@Test
	@DisplayName("Não deve cadastrar uma biometria idêntica")
	void naoDeveCadastrarUmaBiometriaIdentica() throws JsonProcessingException, Exception {
		
		mockMvc.perform(
				post(uri + "/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new BiometriaRequest("fingerPrint"))))
		.andExpect(status().isCreated())
		.andExpect(header().exists(HttpHeaders.LOCATION))
		.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/1"))
		.andDo(print());
		
		mockMvc.perform(
				post(uri + "/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new BiometriaRequest("fingerPrint"))))
		.andExpect(status().isBadRequest())
		.andDo(print());		
	}
	
	@Test
	@DisplayName("Não deve cadastrar uma biometria para um cartão inválido")
	void naoDeveCadastrarUmaBiometriaParaUmCartaoInvalido() throws JsonProcessingException, Exception {		
		
		mockMvc.perform(
				post(uri + "/100")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new BiometriaRequest("fingerPrint"))))
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	
	@Test
	@DisplayName("Não deve cadastrar uma biometria se o campo estiver vazio")
	void naoDeveCadastrarUmaBiometriaSeOCampoEstiverVazio() throws JsonProcessingException, Exception {		
		
		mockMvc.perform(
				post(uri + "/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(new BiometriaRequest(""))))
		.andExpect(status().isBadRequest())
		.andDo(print());
	}

}
