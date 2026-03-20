package rycfod.com.the_tracking_guard_dog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * The "data" object inside a ShipEngine webhook payload.
 * Contains the full tracking snapshot at the moment of the event.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipEngineTrackingData {

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("status_description")
    private String statusDescription;

    /** Raw carrier status code (carrier-specific). */
    @JsonProperty("carrier_status_code")
    private String carrierStatusCode;

    /** Human-readable description provided directly by the carrier. */
    @JsonProperty("carrier_status_description")
    private String carrierStatusDescription;

    @JsonProperty("ship_date")
    private OffsetDateTime shipDate;

    @JsonProperty("estimated_delivery_date")
    private OffsetDateTime estimatedDeliveryDate;

    @JsonProperty("actual_delivery_date")
    private OffsetDateTime actualDeliveryDate;

    @JsonProperty("exception_description")
    private String exceptionDescription;

    @JsonProperty("events")
    private List<ShipEngineTrackingEvent> events;
}
