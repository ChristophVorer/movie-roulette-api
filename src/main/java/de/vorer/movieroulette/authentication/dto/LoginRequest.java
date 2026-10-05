package de.vorer.movieroulette.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Es wurde kein Benutzername übertragen")
        String email,

        @NotBlank(message = "Es wurde kein Passwort übertragen")
        String password
) {
}
