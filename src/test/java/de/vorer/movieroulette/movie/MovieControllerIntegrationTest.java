package de.vorer.movieroulette.movie;

import de.vorer.movieroulette.common.IntegrationTest;
import de.vorer.movieroulette.integration.tmdb.TmdbClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TmdbClient tmdbClient;

    @Test
    void unknownMovieShouldReturnNotFound() throws Exception {
        when(tmdbClient.getMovieById(999_999_999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/999999999").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Film nicht gefunden"))
                .andExpect(jsonPath("$.detail").value("Es wurde kein Film mit der ID 999999999 gefunden."));
    }
}