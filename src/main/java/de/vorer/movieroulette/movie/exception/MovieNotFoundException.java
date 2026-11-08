package de.vorer.movieroulette.movie.exception;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(long movieId) {
        super("Es wurde kein Film mit der ID %d gefunden.".formatted(movieId));
    }
}