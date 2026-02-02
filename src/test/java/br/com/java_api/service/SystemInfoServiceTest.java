package br.com.java_api.service;

import br.com.java_api.dto.ContainerInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SystemInfoServiceTest {

    private SystemInfoService systemInfoService;

    @BeforeEach
    void setUp() {
        systemInfoService = new SystemInfoService();
        ReflectionTestUtils.setField(systemInfoService, "appVersion", "1.0.0-TEST");
    }

    @Test
    void getContainerInfo_DeveRetornarInformacoesDoContainer() {
        // Act
        ContainerInfoDTO result = systemInfoService.getContainerInfo();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getContainerName());
        assertNotNull(result.getIpAddress());
        assertNotNull(result.getOsName());
        assertEquals("1.0.0-TEST", result.getAppVersion());
        assertTrue(result.getUptimeMillis() > 0);
    }

    @Test
    void getContainerName_DeveRetornarNomeDoContainer() {
        // Act
        String result = systemInfoService.getContainerName();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getDetailedStatus_DeveRetornarStatusDetalhado() {
        // Act
        Map<String, Object> result = systemInfoService.getDetailedStatus();

        // Assert
        assertNotNull(result);
        assertEquals("UP", result.get("status"));
        assertNotNull(result.get("hostname"));
        assertNotNull(result.get("ip"));
        assertNotNull(result.get("uptime_sec"));
        assertNotNull(result.get("java_version"));
        assertNotNull(result.get("os"));
        assertNotNull(result.get("timestamp"));
        assertEquals("1.0.0-TEST", result.get("app_version"));
        
        // Verifica estrutura de memória
        Map<String, Object> memory = (Map<String, Object>) result.get("memory");
        assertNotNull(memory);
        assertNotNull(memory.get("heap_used_MB"));
        assertNotNull(memory.get("heap_max_MB"));
        assertNotNull(memory.get("non_heap_used_MB"));
    }

    @Test
    void getUptime_DeveRetornarUptimePositivo() {
        // Act
        long uptime = systemInfoService.getUptime();

        // Assert
        assertTrue(uptime > 0);
    }
}