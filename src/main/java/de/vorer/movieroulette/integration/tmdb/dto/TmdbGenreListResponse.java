package de.vorer.movieroulette.integration.tmdb.dto;

import java.util.List;

public record TmdbGenreListResponse(
        List<TmdbGenreResponse> genres
) {
}