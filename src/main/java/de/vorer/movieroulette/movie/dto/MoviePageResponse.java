package de.vorer.movieroulette.movie.dto;

import java.util.List;

public record MoviePageResponse(
        int page,
        List<MovieResponse> movies,
        int totalPages,
        int totalResults
) {
}