package br.com.java_api.controller;

import br.com.java_api.service.SystemInfoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatusRestController {

    @Autowired
    private SystemInfoService systemInfoService;

    @Operation(summary = "Status detalhado da API em JSON")
    @GetMapping("/api/status")
    public ResponseEntity<Map<String, Object>> apiStatus() {
        Map<String, Object> status = systemInfoService.getDetailedStatus();
        return ResponseEntity.ok(status);
    }
}