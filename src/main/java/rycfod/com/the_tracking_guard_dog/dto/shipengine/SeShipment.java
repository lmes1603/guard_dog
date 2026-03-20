package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Inline shipment block sent inside {@link SeLabelRequest} when no
 * pre-existing {@code shipment_id} is available.
 */
@Data
@Builder
public class SeShipment {

    /** ShipEngine carrier account ID, e.g. "se-123456". */
    @JsonProperty("carrier_id")
    private String carrierId;

    /** Service code, e.g. "usps_priority_mail", "fedex_ground". */
    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("ship_to")
    private SeAddress shipTo;

    @JsonProperty("ship_from")
    private SeAddress shipFrom;

    @JsonProperty("packages")
    private List<SePackage> packages;
}
