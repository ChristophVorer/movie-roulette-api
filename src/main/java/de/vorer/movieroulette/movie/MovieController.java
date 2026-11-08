package de.vorer.movieroulette.movie;

import de.vorer.movieroulette.movie.dto.MoviePageResponse;
import de.vorer.movieroulette.movie.dto.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Movies", description = "Movie data provided by TMDB")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(summary = "Get movie details", description = "Returns a single movie by its TMDB id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid access token", content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "No movie with this id exists",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "TMDB failed or is unreachable",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping("/{movieId}")
    public MovieResponse getMovieById(
            @Parameter(description = "TMDB movie id", example = "550")
            @PathVariable long movieId
    ) {
        return movieService.getMovieById(movieId);
    }

    @Operation(summary = "Get popular movies", description = "Returns a page of currently popular movies from TMDB.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of popular movies"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid page number",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Missing or invalid access token", content = @Content),
            @ApiResponse(
                    responseCode = "502",
                    description = "TMDB failed or is unreachable",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping("/popular")
    public MoviePageResponse getPopularMovies(
            @Parameter(description = "Page number, starting at 1", example = "1")
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "Die Seitennummer muss mindestens 1 betragen.")
            int page
    ) {
        return movieService.getPopularMovies(page);
    }
}