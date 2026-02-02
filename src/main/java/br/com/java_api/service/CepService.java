package br.com.java_api.service;

import br.com.java_api.dto.CepResponseDTO;
import br.com.java_api.exception.CepNotFoundException;
import br.com.java_api.exception.ExternalApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CepService {

    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{8}$");
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${api.viacep.url}")
    private String viaCepUrl;

    public CepResponseDTO buscarEndereco(String cep) {
        String cepLimpo = limparCep(cep);
        validarCep(cepLimpo);
        
        try {
            String url = viaCepUrl + cepLimpo + "/json/";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null || response.containsKey("erro")) {
                throw new CepNotFoundException("CEP não encontrado: " + cep);
            }
            
            return objectMapper.convertValue(response, CepResponseDTO.class);
            
        } catch (RestClientException e) {
            throw new ExternalApiException("Erro ao consultar API ViaCEP", e);
        }
    }

    private String limparCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("CEP não pode ser nulo");
        }
        return cep.replaceAll("[^\\d]", "");
    }

    private void validarCep(String cep) {
        if (!CEP_PATTERN.matcher(cep).matches()) {
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos");
        }
    }
}