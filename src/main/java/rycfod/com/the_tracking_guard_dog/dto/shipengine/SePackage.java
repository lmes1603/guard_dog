package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Represents one package inside the ShipEngine shipment.
 * A shipment can contain multiple packages.
 */
@Data
@Builder
public class SePackage {

    @JsonProperty("weight")
    private SeWeight weight;

    @JsonProperty("dimensions")
    private SeDimensions dimensions;
}
