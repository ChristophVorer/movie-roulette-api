package de.vorer.movieroulette.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "Unique username", example = "christoph")
        @NotBlank(message = "Der Benutzername darf nicht leer sein.")
        String username,

        @Schema(description = "Unique email address", example = "christoph@example.com")
        @NotBlank(message = "Die E-Mail-Adresse darf nicht leer sein.")
        @Email(message = "Die E-Mail-Adresse ist ungültig.")
        String email,

        @Schema(
                description = "8 to 64 characters, with upper and lower case letters, a digit and a special character",
                example = "MeinSicheresPasswort123!"
        )
        @NotBlank(message = "Das Passwort darf nicht leer sein.")
        @Size(min = 8, max = 64, message = "Das Passwort muss zwischen 8 und 64 Zeichen lang sein.")
        @Pattern(
                regexp = "^(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\d)(?=.*[^\\p{L}\\d\\s]).*$",
                message = "Das Passwort muss Groß- und Kleinbuchstaben, eine Zahl und ein Sonderzeichen enthalten."
        )
        String password
) {
}