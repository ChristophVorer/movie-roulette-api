package de.vorer.movieroulette.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "Unique username", example = "christoph")
        @NotBlank(message = "Der Benutzername darf nicht leer sein.")
        String username,

        @Schema(description = "Unique email address", example = "christoph@example.com")
        @NotBlank(message = "Die E-Mail-Adresse darf nicht leer sein.")
        @Email(message = "Die E-Mail-Adresse ist ungültig.")
        String email,

        @Schema(description = "At least 8 characters", example = "MeinSicheresPasswort123!")
        @NotBlank(message = "Das Passwort darf nicht leer sein.")
        @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein.")
        String password
) {
}