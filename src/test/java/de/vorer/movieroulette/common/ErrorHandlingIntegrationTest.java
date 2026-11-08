package de.vorer.movieroulette.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ErrorHandlingIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unknownPathShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/does-not-exist").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void wrongHttpMethodShouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/api/authentication/login"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void malformedJsonShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                        post("/api/authentication/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ invalid json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void invalidRequestParameterShouldReturnValidationErrors() throws Exception {
        mockMvc.perform(get("/api/movies/popular").param("page", "0").with(jwt()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validierungsfehler"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("page"))
                .andExpect(jsonPath("$.validationErrors[0].message")
                        .value("Die Seitennummer muss mindestens 1 betragen."));
    }
}