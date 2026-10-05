package de.vorer.movieroulette.common;

import de.vorer.movieroulette.authentication.exceptions.InvalidCredentialsException;
import de.vorer.movieroulette.common.dto.ValidationError;
import de.vorer.movieroulette.integration.tmdb.exception.TmdbException;
import de.vorer.movieroulette.user.exception.EmailAlreadyExistsException;
import de.vorer.movieroulette.user.exception.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UsernameAlreadyExistsException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Benutzer existiert bereits");
        problem.setDetail(exception.getMessage());

        return problem;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(EmailAlreadyExistsException exception) {
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validierungsfehler");
        problem.setDetail("Die übermittelten Daten sind ungültig.");

        List<ValidationError> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        problem.setProperty("validationErrors", validationErrors);

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
        problem.setDetail("Filmdaten konnten  nicht geladen werden.");

        return problem;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleHandlerMethodValidationException(
            HandlerMethodValidationException exception
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validierungsfehler");
        problem.setDetail("Fehler bei der Parameterübergabe.");

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
}