package br.com.java_api.service;

import br.com.java_api.dto.CepResponseDTO;
import br.com.java_api.exception.CepNotFoundException;
import br.com.java_api.exception.ExternalApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CepService cepService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cepService, "viaCepUrl", "https://viacep.com.br/ws/");
    }

    @Test
    void buscarEndereco_ComCepValido_DeveRetornarCepResponseDTO() {
        // Arrange
        String cep = "01001000";
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("cep", "01001-000");
        apiResponse.put("logradouro", "Praça da Sé");
        apiResponse.put("bairro", "Sé");
        apiResponse.put("localidade", "São Paulo");
        apiResponse.put("uf", "SP");

        CepResponseDTO expectedDto = new CepResponseDTO();
        expectedDto.setCep("01001-000");
        expectedDto.setLogradouro("Praça da Sé");

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(apiResponse);
        when(objectMapper.convertValue(apiResponse, CepResponseDTO.class)).thenReturn(expectedDto);

        // Act
        CepResponseDTO result = cepService.buscarEndereco(cep);

        // Assert
        assertNotNull(result);
        assertEquals("01001-000", result.getCep());
        assertEquals("Praça da Sé", result.getLogradouro());
        verify(restTemplate).getForObject("https://viacep.com.br/ws/01001000/json/", Map.class);
    }

    @Test
    void buscarEndereco_ComCepInexistente_DeveLancarCepNotFoundException() {
        // Arrange
        String cep = "99999999";
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("erro", true);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(apiResponse);

        // Act & Assert
        assertThrows(CepNotFoundException.class, () -> cepService.buscarEndereco(cep));
    }

    @Test
    void buscarEndereco_ComCepInvalido_DeveLancarIllegalArgumentException() {
        // Arrange
        String cepInvalido = "123";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cepService.buscarEndereco(cepInvalido));
    }

    @Test
    void buscarEndereco_ComCepNulo_DeveLancarIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cepService.buscarEndereco(null));
    }

    @Test
    void buscarEndereco_ComErroNaAPI_DeveLancarExternalApiException() {
        // Arrange
        String cep = "01001000";
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenThrow(new RestClientException("Erro de conexão"));

        // Act & Assert
        assertThrows(ExternalApiException.class, () -> cepService.buscarEndereco(cep));
    }

    @Test
    void buscarEndereco_ComCepComMascara_DeveRemoverMascaraEBuscar() {
        // Arrange
        String cepComMascara = "01001-000";
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("cep", "01001-000");
        
        CepResponseDTO expectedDto = new CepResponseDTO();
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(apiResponse);
        when(objectMapper.convertValue(apiResponse, CepResponseDTO.class)).thenReturn(expectedDto);

        // Act
        cepService.buscarEndereco(cepComMascara);

        // Assert
        verify(restTemplate).getForObject("https://viacep.com.br/ws/01001000/json/", Map.class);
    }
}