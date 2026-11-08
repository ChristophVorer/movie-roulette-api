package de.vorer.movieroulette.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MovieResponse(
        @Schema(description = "TMDB movie id", example = "550")
        long id,

        @Schema(example = "Fight Club")
        String title,

        @Schema(example = "1999-10-15")
        LocalDate releaseDate,

        @Schema(description = "Full URL of the poster image, null if TMDB has no poster",
                example = "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg")
        String posterUrl,

        @Schema(description = "Genre names in German", example = "[\"Drama\", \"Thriller\"]")
        List<String> genres
) {
}