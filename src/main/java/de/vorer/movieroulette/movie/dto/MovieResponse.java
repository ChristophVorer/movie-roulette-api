package de.vorer.movieroulette.movie.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieResponse(
        long id,
        String title,
        LocalDate releaseDate,
        String posterUrl,
        List<String> genres
) {
}