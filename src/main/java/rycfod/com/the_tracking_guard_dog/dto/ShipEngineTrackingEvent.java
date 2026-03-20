package rycfod.com.the_tracking_guard_dog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Represents a single tracking scan event inside the ShipEngine payload.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipEngineTrackingEvent {

    @JsonProperty("occurred_at")
    private OffsetDateTime occurredAt;

    @JsonProperty("carrier_occurred_at")
    private String carrierOccurredAt;

    @JsonProperty("description")
    private String description;

    @JsonProperty("city_locality")
    private String cityLocality;

    @JsonProperty("state_province")
    private String stateProvince;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("signer")
    private String signer;

    @JsonProperty("event_code")
    private String eventCode;

    @JsonProperty("status_code")
    private String statusCode;
}
