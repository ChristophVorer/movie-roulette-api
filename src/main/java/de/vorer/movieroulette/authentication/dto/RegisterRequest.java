package de.vorer.movieroulette.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Der Benutzername darf nicht leer sein.")
        String username,

        @NotBlank(message = "Die E-Mail-Adresse darf nicht leer sein.")
        @Email(message = "Die E-Mail-Adresse ist ungültig.")
        String email,

        @NotBlank(message = "Das Passwort darf nicht leer sein.")
        @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein.")
        String password
) {}
