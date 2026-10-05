package de.vorer.movieroulette.movie;

import de.vorer.movieroulette.integration.tmdb.TmdbClient;
import de.vorer.movieroulette.integration.tmdb.TmdbProperties;
import de.vorer.movieroulette.integration.tmdb.TmdbReferenceDataService;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbGenreResponse;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbMovieListItemResponse;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbMovieListResponse;
import de.vorer.movieroulette.integration.tmdb.dto.TmdbMovieResponse;
import de.vorer.movieroulette.movie.dto.MoviePageResponse;
import de.vorer.movieroulette.movie.dto.MovieResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MovieService {

    private final TmdbClient tmdbClient;
    private final TmdbProperties tmdbProperties;
    private final TmdbReferenceDataService tmdbReferenceDataService;

    public MovieService(
            TmdbClient tmdbClient,
            TmdbProperties tmdbProperties,
            TmdbReferenceDataService tmdbReferenceDataService
    ) {
        this.tmdbClient = tmdbClient;
        this.tmdbProperties = tmdbProperties;
        this.tmdbReferenceDataService = tmdbReferenceDataService;
    }

    public MovieResponse getMovieById(long movieId) {
        TmdbMovieResponse tmdbMovie = tmdbClient.getMovieById(movieId);

        return new MovieResponse(
                tmdbMovie.id(),
                tmdbMovie.title(),
                tmdbMovie.releaseDate(),
                buildPosterUrl(tmdbMovie.posterPath()),
                tmdbMovie.genres()
                        .stream()
                        .map(TmdbGenreResponse::name)
                        .toList()
        );
    }

    public MoviePageResponse getPopularMovies(int page) {
        TmdbMovieListResponse movieList = tmdbClient.getPopularMovies(page);
        Map<Long, String> genresById = tmdbReferenceDataService.getMovieGenres();

        List<MovieResponse> movies = movieList.results()
                .stream()
                .map(movie -> mapMovie(movie, genresById))
                .toList();

        return new MoviePageResponse(
                movieList.page(),
                movies,
                movieList.totalPages(),
                movieList.totalResults()
        );
    }

    private MovieResponse mapMovie(TmdbMovieListItemResponse movie, Map<Long, String> genresById) {
        List<String> genres = movie.genreIds()
                .stream()
                .map(genresById::get)
                .filter(Objects::nonNull)
                .toList();

        return new MovieResponse(
                movie.id(),
                movie.title(),
                movie.releaseDate(),
                buildPosterUrl(movie.posterPath()),
                genres
        );
    }

    private String buildPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isBlank()) {
            return null;
        }

        return "%s/%s%s".formatted(
                tmdbProperties.imageBaseUrl(),
                tmdbProperties.posterSize(),
                posterPath
        );
    }
}