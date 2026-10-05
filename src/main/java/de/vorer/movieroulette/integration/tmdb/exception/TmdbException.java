package de.vorer.movieroulette.integration.tmdb.exception;

public class TmdbException extends RuntimeException {

    private final Integer statusCode;

    public TmdbException(Integer statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}