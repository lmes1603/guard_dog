package rycfod.com.the_tracking_guard_dog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Acknowledgement body returned by the ShipEngine webhook endpoint.
 * Gives Postman and any monitoring tool a clear confirmation of receipt.
 */
public record WebhookAckResponse(
        String status,
        @JsonProperty("received_at") Instant receivedAt,
        @JsonProperty("resource_type") String resourceType
) {
    public static WebhookAckResponse of(String resourceType) {
        return new WebhookAckResponse("received", Instant.now(), resourceType);
    }
}
