package de.vorer.movieroulette.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponse(
        @Schema(description = "JWT access token (RS256), valid for 15 minutes", example = "eyJhbGciOiJSUzI1NiJ9...")
        String accessToken
) {
}