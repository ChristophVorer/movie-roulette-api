package de.vorer.movieroulette.integration.tmdb;

import de.vorer.movieroulette.integration.tmdb.dto.TmdbGenreListResponse;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbMovieListResponse;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbMovieResponse;
import de.vorer.movieroulette.integration.tmdb.exception.TmdbException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class TmdbClient {

    private final RestClient restClient;

    public TmdbClient(RestClient tmdbRestClient) {
        this.restClient = tmdbRestClient;
    }

    public TmdbMovieResponse getMovieById(long movieId) {
        try {
            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{movieId}")
                            .queryParam("language", "de-DE")
                            .build(movieId)
                    )
                    .retrieve()
                    .body(TmdbMovieResponse.class);
        } catch (HttpStatusCodeException exception) {
            throw mapHttpException(exception);
        } catch (RestClientException exception) {
            throw mapClientException(exception);
        }
    }

    public TmdbMovieListResponse getPopularMovies(int page) {
        try {
            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("language", "de-DE")
                            .queryParam("page", page)
                            .build()
                    )
                    .retrieve()
                    .body(TmdbMovieListResponse.class);
        } catch (HttpStatusCodeException exception) {
            throw mapHttpException(exception);
        } catch (RestClientException exception) {
            throw mapClientException(exception);
        }
    }

    public TmdbGenreListResponse getMovieGenres() {
        try {
            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/genre/movie/list")
                            .queryParam("language", "de-DE")
                            .build()
                    )
                    .retrieve()
                    .body(TmdbGenreListResponse.class);
        } catch (HttpStatusCodeException exception) {
            throw mapHttpException(exception);
        } catch (RestClientException exception) {
            throw mapClientException(exception);
        }
    }

    private TmdbException mapHttpException(HttpStatusCodeException exception) {
        return new TmdbException(
                exception.getStatusCode().value(),
                "TMDB failed with status " + exception.getStatusCode().value(),
                exception
        );
    }

    private TmdbException mapClientException(RestClientException exception) {
        return new TmdbException(
                null,
                "TMDB is currently unreachable.",
                exception
        );
    }
}