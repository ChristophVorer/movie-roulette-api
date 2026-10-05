package de.vorer.movieroulette.common.dto;

public record ValidationError(
        String field,
        String message
) {
}
