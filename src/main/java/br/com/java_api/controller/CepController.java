package br.com.java_api.controller;

import br.com.java_api.dto.CepResponseDTO;
import br.com.java_api.service.CepService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/cep")
@Validated
public class CepController {

    @Autowired
    private CepService cepService;

    @Operation(summary = "Busca endereço a partir do CEP (formato 01001000)")
    @GetMapping("/{cep}")
    public ResponseEntity<CepResponseDTO> buscarEndereco(
            @PathVariable 
            @Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 dígitos") 
            String cep) {
        
        CepResponseDTO response = cepService.buscarEndereco(cep);
        return ResponseEntity.ok(response);
    }
}