package de.vorer.movieroulette.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "christoph@example.com")
        @NotBlank(message = "Die E-Mail-Adresse darf nicht leer sein.")
        String email,

        @Schema(example = "MeinSicheresPasswort123!")
        @NotBlank(message = "Das Passwort darf nicht leer sein.")
        String password
) {
}