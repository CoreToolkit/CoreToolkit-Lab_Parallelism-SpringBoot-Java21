package edu.eci.arsw.parallelism.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests verifying the complete application works together
 * Uses MockMvc which provides integration testing of Spring MVC controllers
 * while still using mock implementations of HTTP requests.
 *
 * These tests verify:
 * - Swagger UI is accessible
 * - OpenAPI documentation is generated
 * - API endpoints work correctly
 * - Error handling works as expected
 */
@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void swaggerUi_IsAccessible() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void apiDocs_ReturnsValidOpenApiJson() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").exists())
                .andExpect(jsonPath("$.paths").exists())
                .andExpect(jsonPath("$.paths['/api/v1/pi/digits']").exists());
    }

    @Test
    void apiEndpoint_ReturnsSuccessWithValidParameters() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(0))
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.digits").exists())
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void apiEndpoint_HandlesSingleDigitRequest() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.digits").isString());
    }

    @Test
    void apiEndpoint_ReturnsBadRequestForNegativeStart() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "-1")
                        .param("count", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apiEndpoint_ReturnsBadRequestForZeroCount() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0")
                        .param("count", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apiEndpoint_ReturnsBadRequestForMissingStartParameter() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("count", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apiEndpoint_ReturnsBadRequestForMissingCountParameter() throws Exception {
        mockMvc.perform(get("/api/v1/pi/digits")
                        .param("start", "0"))
                .andExpect(status().isBadRequest());
    }


}