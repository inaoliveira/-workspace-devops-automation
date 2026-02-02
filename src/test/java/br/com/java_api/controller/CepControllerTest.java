package br.com.java_api.controller;

import br.com.java_api.dto.CepResponseDTO;
import br.com.java_api.exception.CepNotFoundException;
import br.com.java_api.service.CepService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CepController.class)
class CepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CepService cepService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void buscarEndereco_ComCepValido_DeveRetornar200() throws Exception {
        // Arrange
        CepResponseDTO cepResponse = new CepResponseDTO();
        cepResponse.setCep("01001-000");
        cepResponse.setLogradouro("Praça da Sé");
        cepResponse.setBairro("Sé");
        cepResponse.setLocalidade("São Paulo");
        cepResponse.setUf("SP");

        when(cepService.buscarEndereco("01001000")).thenReturn(cepResponse);

        // Act & Assert
        mockMvc.perform(get("/api/cep/01001000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cep").value("01001-000"))
                .andExpect(jsonPath("$.logradouro").value("Praça da Sé"))
                .andExpect(jsonPath("$.bairro").value("Sé"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"))
                .andExpect(jsonPath("$.uf").value("SP"));
    }

    @Test
    void buscarEndereco_ComCepInexistente_DeveRetornar404() throws Exception {
        // Arrange
        when(cepService.buscarEndereco(anyString()))
                .thenThrow(new CepNotFoundException("CEP não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/cep/99999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("CEP não encontrado"));
    }

    @Test
    void buscarEndereco_ComCepInvalido_DeveRetornar400() throws Exception {
        // Arrange
        when(cepService.buscarEndereco(anyString()))
                .thenThrow(new IllegalArgumentException("CEP deve conter exatamente 8 dígitos"));

        // Act & Assert
        mockMvc.perform(get("/api/cep/123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Dados inválidos"));
    }
}