package de.vorer.movieroulette.integration.tmdb;

import de.vorer.movieroulette.integration.tmdb.dto.TmdbGenreResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TmdbReferenceDataService {

    private final TmdbClient tmdbClient;

    public TmdbReferenceDataService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    @Cacheable("tmdbGenres")
    public Map<Long, String> getMovieGenres() {
        return tmdbClient
                .getMovieGenres()
                .genres()
                .stream()
                .collect(Collectors.toMap(
                        TmdbGenreResponse::id,
                        TmdbGenreResponse::name
                ));
    }
}