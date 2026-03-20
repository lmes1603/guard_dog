package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/** Package weight. Unit: "ounce", "pound", "gram", "kilogram". */
@Data
@Builder
public class SeWeight {

    @JsonProperty("value")
    private double value;

    @JsonProperty("unit")
    private String unit;
}
