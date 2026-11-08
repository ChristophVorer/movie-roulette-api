package de.vorer.movieroulette.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ValidationError(
        @Schema(description = "Name of the invalid field", example = "email")
        String field,

        @Schema(description = "Human-readable error message", example = "Die E-Mail-Adresse ist ungültig.")
        String message
) {
}