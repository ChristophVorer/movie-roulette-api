package de.vorer.movieroulette.common;

import de.vorer.movieroulette.authentication.exceptions.InvalidCredentialsException;
import de.vorer.movieroulette.common.dto.ValidationError;
import de.vorer.movieroulette.integration.tmdb.exception.TmdbException;
import de.vorer.movieroulette.movie.exception.MovieNotFoundException;
import de.vorer.movieroulette.user.exception.EmailAlreadyExistsException;
import de.vorer.movieroulette.user.exception.UsernameAlreadyExistsException;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ProblemDetail handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Benutzer existiert bereits");
        problem.setDetail(exception.getMessage());

        return problem;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("E-Mail-Adresse existiert bereits");
        problem.setDetail(exception.getMessage());

        return problem;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Authentifizierung fehlgeschlagen");
        problem.setDetail(exception.getMessage());

        return problem;
    }

    @ExceptionHandler(TmdbException.class)
    public ProblemDetail handleTmdbException(TmdbException exception) {
        log.error(
                "Fehler bei der Kommunikation mit TMDB. Status: {}",
                exception.getStatusCode(),
                exception
        );

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problem.setTitle("Externer Dienst nicht verfügbar");
        problem.setDetail("Filmdaten konnten nicht geladen werden.");

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception exception) {
        log.error("Unerwarteter Fehler", exception);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Interner Serverfehler");
        problem.setDetail("Es ist ein unerwarteter Fehler aufgetreten.");

        return problem;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<ValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        ProblemDetail problem = validationProblem("Die übermittelten Daten sind ungültig.", validationErrors);

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<ValidationError> validationErrors = exception.getParameterValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors()
                        .stream()
                        .map(error -> new ValidationError(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage()
                        )))
                .toList();

        ProblemDetail problem = validationProblem("Fehler bei der Parameterübergabe.", validationErrors);

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    private ProblemDetail validationProblem(String detail, List<ValidationError> validationErrors) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validierungsfehler");
        problem.setDetail(detail);
        problem.setProperty("validationErrors", validationErrors);

        return problem;
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ProblemDetail handleMovieNotFound(MovieNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Film nicht gefunden");
        problem.setDetail(exception.getMessage());

        return problem;
    }
}