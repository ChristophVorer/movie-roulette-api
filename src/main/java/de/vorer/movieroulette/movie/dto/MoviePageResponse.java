package de.vorer.movieroulette.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MoviePageResponse(
        @Schema(description = "Current page, starting at 1", example = "1")
        int page,

        List<MovieResponse> movies,

        @Schema(example = "500")
        int totalPages,

        @Schema(example = "10000")
        int totalResults
) {
}