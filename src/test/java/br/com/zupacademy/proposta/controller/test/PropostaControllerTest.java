package br.com.zupacademy.proposta.controller.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zupacademy.proposta.models.request.PropostaRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class PropostaControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper jsonMapper;

	private String json(PropostaRequest request) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(request);
	}

	@Test	
	void deveCriarUmaNovaProposta() throws Exception {		
		PropostaRequest propostaRequest = new PropostaRequest("91994996064", "fulano@gmail.com", "Fulano", "Rua X", 2500.0);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
				.content(json(propostaRequest))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION,("http://localhost/propostas/1")));
		
		String propostaRequestJson = jsonMapper.writeValueAsString(propostaRequest);		
		System.out.println(propostaRequestJson);
		System.out.println(jsonMapper.readValue(propostaRequestJson, PropostaRequest.class));
	}

}

