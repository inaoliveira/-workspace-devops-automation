package br.com.java_api.controller;

import br.com.java_api.dto.ContainerInfoDTO;
import br.com.java_api.service.SystemInfoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private SystemInfoService systemInfoService;

    @Operation(summary = "Hello Basico")
    @GetMapping("/api/hello")
    public String hello() {
        return "Ola, Mundo da API!";
    }

    @Operation(summary = "Retorna o nome (Hostname) do container")
    @GetMapping("/api/getContainerName")
    public ResponseEntity<String> getContainerName() {
        String hostname = systemInfoService.getContainerName();
        return ResponseEntity.ok("Container name (hostname): " + hostname);
    }

    @Operation(summary = "Exibe informações gerais do container")
    @GetMapping("/api/info")
    public ResponseEntity<ContainerInfoDTO> getContainerInfo() {
        ContainerInfoDTO info = systemInfoService.getContainerInfo();
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Retorna JSON estático do resources/static")
    @GetMapping(value = "/api/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getJsonFromStatic() {
        try {
            ClassPathResource resource = new ClassPathResource("static/device.json");
            String json = new String(resource.getInputStream().readAllBytes());
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\": \"Erro ao carregar JSON: " + e.getMessage() + "\"}");
        }
    }
}
