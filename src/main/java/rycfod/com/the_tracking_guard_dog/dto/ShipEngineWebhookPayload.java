package rycfod.com.the_tracking_guard_dog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Root DTO for the ShipEngine tracking webhook POST body.
 *
 * Example payload:
 * {
 *   "resource_url": "https://api.shipengine.com/v1/tracking?...",
 *   "resource_type": "API_RESULT",
 *   "data": { ... }
 * }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipEngineWebhookPayload {

    @JsonProperty("resource_url")
    private String resourceUrl;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("data")
    private ShipEngineTrackingData data;
}
