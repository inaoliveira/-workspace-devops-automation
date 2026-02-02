package br.com.java_api.controller;

import br.com.java_api.dto.ContainerInfoDTO;
import br.com.java_api.service.SystemInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SystemInfoService systemInfoService;

    @Test
    void hello_DeveRetornarMensagemOla() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ola, Mundo da API!"));
    }

    @Test
    void getContainerName_DeveRetornarNomeDoContainer() throws Exception {
        // Arrange
        when(systemInfoService.getContainerName()).thenReturn("localhost");

        // Act & Assert
        mockMvc.perform(get("/api/getContainerName"))
                .andExpect(status().isOk())
                .andExpect(content().string("Container name (hostname): localhost"));
    }

    @Test
    void getContainerInfo_DeveRetornarInformacoesDoContainer() throws Exception {
        // Arrange
        ContainerInfoDTO containerInfo = new ContainerInfoDTO("localhost", "127.0.0.1", "Linux", "1.0.0", 123456L);
        when(systemInfoService.getContainerInfo()).thenReturn(containerInfo);

        // Act & Assert
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.containerName").value("localhost"))
                .andExpect(jsonPath("$.ipAddress").value("127.0.0.1"))
                .andExpect(jsonPath("$.osName").value("Linux"))
                .andExpect(jsonPath("$.appVersion").value("1.0.0"))
                .andExpect(jsonPath("$.uptimeMillis").value(123456L));
    }

    @Test
    void getJsonFromStatic_DeveRetornarJson() throws Exception {
        mockMvc.perform(get("/api/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}