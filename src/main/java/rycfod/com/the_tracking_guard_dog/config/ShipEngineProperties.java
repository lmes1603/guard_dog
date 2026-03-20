package rycfod.com.the_tracking_guard_dog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Typed binding for all "shipengine.*" keys in application.properties.
 *
 * <p>Prefer {@code @ConfigurationProperties} over scattered {@code @Value}:
 * one place to change, full IDE auto-complete, and validation support.</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "shipengine")
public class ShipEngineProperties {

    /** ShipEngine API key — read from application.properties, never hardcoded. */
    private String apiKey;

    /** Base URL; swap to https://api.shipengine.com for production. */
    private String baseUrl;

    private int connectTimeoutMs = 5_000;
    private int readTimeoutMs    = 10_000;
}
