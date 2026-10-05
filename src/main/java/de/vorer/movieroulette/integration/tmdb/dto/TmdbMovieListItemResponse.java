package de.vorer.movieroulette.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record TmdbMovieListItemResponse(
        long id,
        String title,

        @JsonProperty("release_date")
        LocalDate releaseDate,

        @JsonProperty("poster_path")
        String posterPath,

        @JsonProperty("genre_ids")
        List<Long> genreIds
) {
}