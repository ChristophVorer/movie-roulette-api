package de.vorer.movieroulette.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "christoph@example.com")
        @NotBlank(message = "Es wurde kein Benutzername übertragen")
        String email,

        @Schema(example = "MeinSicheresPasswort123!")
        @NotBlank(message = "Es wurde kein Passwort übertragen")
        String password
) {
}