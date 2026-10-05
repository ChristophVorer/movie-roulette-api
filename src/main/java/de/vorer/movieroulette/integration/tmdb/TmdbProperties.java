package de.vorer.movieroulette.integration.tmdb;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "tmdb")
public record TmdbProperties(
        String baseUrl,
        String accessToken,
        Duration connectTimeout,
        Duration readTimeout,
        String imageBaseUrl,
        String posterSize
) {
    public TmdbProperties {
        if (connectTimeout == null) {
            connectTimeout = Duration.ofSeconds(3);
        }

        if (readTimeout == null) {
            readTimeout = Duration.ofSeconds(5);
        }
    }
}