package de.vorer.movieroulette.authentication;

import de.vorer.movieroulette.common.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testEndpointShouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEndpointShouldAcceptAuthenticatedRequest() throws Exception {
        mockMvc.perform(
                        get("/api/test")
                                .with(jwt()
                                        .jwt(jwt -> jwt
                                                .subject("42")
                                                .claim("username", "christoph")
                                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    void testEndpointShouldRejectInvalidToken() throws Exception {
        mockMvc.perform(
                        get("/api/test")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer invalid-token"
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerShouldReturnCreated() throws Exception {
        mockMvc.perform(
                        post("/api/authentication/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "christoph",
                                          "email": "christoph@example.com",
                                          "password": "MeinSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void registerShouldReturnBadRequestForInvalidData() throws Exception {
        mockMvc.perform(
                        post("/api/authentication/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "",
                                          "email": "keine-email",
                                          "password": "123"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validierungsfehler"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    void registerShouldReturnConflictWhenUsernameAlreadyExists() throws Exception {
        registerUser("christoph", "christoph@example.com");

        mockMvc.perform(
                        post("/api/authentication/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "christoph",
                                          "email": "other@example.com",
                                          "password": "AnderesSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void registerShouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        registerUser("christoph", "christoph@example.com");

        mockMvc.perform(
                        post("/api/authentication/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "anderer-user",
                                          "email": "christoph@example.com",
                                          "password": "AnderesSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void loginShouldReturnAccessTokenForValidCredentials() throws Exception {
        registerUser("christoph", "christoph@example.com");

        mockMvc.perform(
                        post("/api/authentication/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "christoph@example.com",
                                          "password": "MeinSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void loginShouldReturnUnauthorizedForWrongPassword() throws Exception {
        registerUser("christoph", "christoph@example.com");

        mockMvc.perform(
                        post("/api/authentication/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "christoph@example.com",
                                          "password": "FalschesPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginShouldReturnUnauthorizedForUnknownUser() throws Exception {
        mockMvc.perform(
                        post("/api/authentication/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "unbekannt",
                                          "password": "MeinSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registeredUserShouldAccessProtectedEndpointWithReturnedToken() throws Exception {
        String response = registerUser("jwt-test-user", "jwt-test@example.com");

        String accessToken = objectMapper
                .readTree(response)
                .get("accessToken")
                .asString();

        mockMvc.perform(
                        get("/api/test")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void loggedInUserShouldAccessProtectedEndpointWithReturnedToken() throws Exception {
        registerUser("login-jwt-user", "login-jwt@example.com");

        String response = mockMvc.perform(
                        post("/api/authentication/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "login-jwt@example.com",
                                          "password": "MeinSicheresPasswort123!"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String accessToken = objectMapper.readTree(response).get("accessToken").asString();

        mockMvc.perform(get("/api/test")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + accessToken
                                ))
                .andExpect(status().isOk());
    }

    private String registerUser(String username, String email) throws Exception {
        return mockMvc.perform(
                        post("/api/authentication/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "username": "%s",
                                          "email": "%s",
                                          "password": "%s"
                                        }
                                        """.formatted(
                                        username,
                                        email,
                                        "MeinSicheresPasswort123!"
                                ))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}