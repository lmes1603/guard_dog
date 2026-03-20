package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/** Package dimensions. Unit: "inch" or "centimeter". */
@Data
@Builder
public class SeDimensions {

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("length")
    private double length;

    @JsonProperty("width")
    private double width;

    @JsonProperty("height")
    private double height;
}
