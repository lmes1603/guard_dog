package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Wire-format request body sent to {@code POST /v1/labels}.
 *
 * <p>Two usage modes:</p>
 * <ul>
 *   <li><b>Existing shipment</b>: populate only {@code shipmentId}.</li>
 *   <li><b>Inline shipment</b>: leave {@code shipmentId} null and populate
 *       {@code shipment} with full address + package details.</li>
 * </ul>
 *
 * {@code @JsonInclude(NON_NULL)} prevents null fields from being serialised,
 * keeping the request body clean for both modes.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeLabelRequest {

    /** Use an existing shipment created via POST /v1/shipments. */
    @JsonProperty("shipment_id")
    private String shipmentId;

    /** Inline shipment — used when there is no pre-created shipment. */
    @JsonProperty("shipment")
    private SeShipment shipment;

    /** "pdf" (default), "zpl", or "png". */
    @JsonProperty("label_format")
    @Builder.Default
    private String labelFormat = "pdf";

    /** "4x6" (default) or "letter". */
    @JsonProperty("label_layout")
    @Builder.Default
    private String labelLayout = "4x6";
}
