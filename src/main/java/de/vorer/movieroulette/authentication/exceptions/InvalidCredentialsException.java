package de.vorer.movieroulette.authentication.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Ungültige E-Mail oder Passwort. Bitte versuche es erneut.");
    }
}
