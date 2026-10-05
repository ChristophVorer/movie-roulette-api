package de.vorer.movieroulette.movie;

import de.vorer.movieroulette.movie.dto.MoviePageResponse;
import de.vorer.movieroulette.movie.dto.MovieResponse;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}")
    public MovieResponse getMovieById(@PathVariable long movieId) {
        return movieService.getMovieById(movieId);
    }

    @GetMapping("/popular")
    public MoviePageResponse getPopularMovie(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "Die Seitennummer muss mindestens 1 betragen.")
            int page
    ) {
        return movieService.getPopularMovies(page);
    }
}