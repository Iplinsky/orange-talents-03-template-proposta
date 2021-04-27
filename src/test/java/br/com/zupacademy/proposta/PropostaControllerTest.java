package br.com.zupacademy.proposta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.proposta.enums.EstadoProposta;
import br.com.zupacademy.proposta.models.Proposta;
import br.com.zupacademy.proposta.models.request.PropostaRequest;
import br.com.zupacademy.proposta.repository.PropostaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class PropostaControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	PropostaRepository propostaRepository; 

	URI uri;
	
	@BeforeEach
	void setUp() throws URISyntaxException {
		uri = new URI("http://localhost:8080/propostas");		
	}	
	
	private String convertToJson(PropostaRequest request) throws JsonProcessingException {
		return mapper.writeValueAsString(request);
	}

	@Test
	@DisplayName("Deve criar uma nova Proposta")
	void deveCriarUmaNovaProposta() throws Exception {
		PropostaRequest propostaRequest = new PropostaRequest("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);

		mockMvc.perform(MockMvcRequestBuilders
					.post(uri)
					.content(convertToJson(propostaRequest))
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(header().string(HttpHeaders.LOCATION, ("http://localhost:8080/propostas/3")))
				.andDo(print());
		}
	
	@Test
	@DisplayName("Não deve ser possível lançar uma proposta idêntica")
	void naoDeveSerPossivelLancarUmaPropostaIdentica() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest = new PropostaRequest("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);

		mockMvc.perform(MockMvcRequestBuilders
					.post(uri)
					.content(convertToJson(propostaRequest))
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())				
				.andExpect(header().exists("Location"))
				.andExpect(header().string(HttpHeaders.LOCATION, ("http://localhost:8080/propostas/3")))
				.andDo(print());
		
		mockMvc.perform(MockMvcRequestBuilders
					.post(uri)
					.content(convertToJson(propostaRequest))
					.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())				
				.andDo(print());
	}
	
	@Test
	@DisplayName("Não deve ser possível criar uma proposta com o número do documento inválido")
	void naoDeveSerPossivelCriarUmaPropostaComONumeroDoDocumentoInvalido() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest1 = new PropostaRequest("Sera_que_vai_falhar?!", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		PropostaRequest propostaRequest2 = new PropostaRequest("", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		PropostaRequest propostaRequest3 = new PropostaRequest("11111111111", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		
		// INVALID TEXT 
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest1))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

		// INVALID EMPTY FIELD
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest2))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

		// INVALID DOCUMENT FORMAT
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest3))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Não deve ser possível criar uma proposta com um email inválido")
	void naoDeveSerPossivelCriarUmaPropostaComUmEmailInvalido() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest1 = new PropostaRequest("91994996064", "", "Fulano", "Rua X", 2500.0);
		PropostaRequest propostaRequest2 = new PropostaRequest("91994996064", "email_testegmail.com", "Fulano", "Rua X", 2500.0);
		PropostaRequest propostaRequest3 = new PropostaRequest("91994996064", "@", "Fulano", "Rua X", 2500.0);
		
		// INVALID EMPTY FIELD
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest1))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
		
		// INVALID EMAIL FORMAT
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest2))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
		
		// INVALID ONLY @
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest3))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Não deve ser possível criar uma proposta com o campo nome em branco")
	void naoDeveSerPossivelCriarUmaPropostaComOCampoNomeEmBranco() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest = new PropostaRequest("91994996064", "email_teste@gmail.com", "", "Rua X", 2500.0);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());		
	}
	
	@Test
	@DisplayName("Não deve ser possível criar uma proposta com o campo endereço em branco")
	void naoDeveSerPossivelCriarUmaPropostaComOCampoEnderecoEmBranco() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest = new PropostaRequest("91994996064", "email_teste@gmail.com", "Fulano", "", 2500.0);
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());		
	}

	@Test
	@DisplayName("Não deve ser possível criar uma proposta com o valor do salário nulo ou negativo")
	void naoDeveSerPossivelCriarUmaPropostaComOValorDoSalarioNuloOuNegativo() throws JsonProcessingException, Exception {
		PropostaRequest propostaRequest1 = new PropostaRequest("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", null);
		PropostaRequest propostaRequest2 = new PropostaRequest("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", -2500.0);
		
		// INVALID NULL INPUT
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest1))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());		

		// INVALID NEGATIVE INPUT
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(convertToJson(propostaRequest2))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());		
	}
	
	@Transactional
	@Rollback
	@Test
	@DisplayName("Deve ser capaz de recuperar a proposta com base no identificador enviado pela URI")
	void deveRecuperarAPropostaComComBaseNoIdentificarEnviadoNaUri() throws JsonProcessingException, Exception {
		Proposta proposta = new Proposta("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		propostaRepository.save(proposta);		
				
		MvcResult mockProposta = mockMvc.perform(MockMvcRequestBuilders
				.get(uri + "/consulta/{id}", proposta.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();		
		
		Proposta propostaMockResponse = mapper.readValue(mockProposta.getResponse().getContentAsString(), Proposta.class);		
		assertEquals(propostaMockResponse, proposta);
	}
	
	@Transactional
	@Rollback
	@Test
	@DisplayName("Não deve retornar a proposta se o identificador for inválido")
	void naoDeveRetornarAPropostaSeOIdentificadorForInvalido() throws Exception {
		Proposta proposta = new Proposta("91994996064", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		propostaRepository.save(proposta);
		
		mockMvc.perform(MockMvcRequestBuilders
				// INVALID ID(2)
				.get(uri + "/consulta/{id}", 100)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve retornar o status da proposta como inelegivel caso o documento inicie com o número três")
	void deveRetornarOStatusDaPropostaComoInelegivelSeONumeroDoDocumentoComecarComTres() throws Exception {
		PropostaRequest propostaRequest = new PropostaRequest("30505139006", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		
		mockMvc.perform(
				 post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertToJson(propostaRequest)))
		.andExpect(status().isCreated())
		.andDo(print());
		
		EstadoProposta estadoDaProposta = propostaRepository.findByEmail(propostaRequest.getEmail()).getEstadoDaProposta();		
		assertEquals(EstadoProposta.NAO_ELEGIVEL, estadoDaProposta);
	}
	
	@Test
	@DisplayName("Deve retornar o status da proposta como elegível caso o documento não iniciar com o número três")
	void deveRetornarOStatusDaPropostaComoElegivelSeONumeroDoDocumentoNaoComecarComTres() throws Exception {
		PropostaRequest propostaRequest = new PropostaRequest("93759865003", "email_teste@gmail.com", "Fulano", "Rua X", 2500.0);
		
		mockMvc.perform(
				 post(uri)
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(convertToJson(propostaRequest)))
		.andExpect(status().isCreated())
		.andDo(print());
		
		EstadoProposta estadoDaProposta = propostaRepository.findByEmail(propostaRequest.getEmail()).getEstadoDaProposta();
		assertEquals(EstadoProposta.ELEGIVEL, estadoDaProposta);		
	}
	
}