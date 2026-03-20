package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Wire-format response from {@code POST /v1/labels}.
 * Only the fields relevant to the Guard Dog are mapped; unknown fields
 * are silently ignored via {@code @JsonIgnoreProperties}.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeLabelResponse {

    @JsonProperty("label_id")
    private String labelId;

    /** "completed" | "processing" | "error". */
    @JsonProperty("status")
    private String status;

    @JsonProperty("shipment_id")
    private String shipmentId;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("carrier_id")
    private String carrierId;

    @JsonProperty("carrier_code")
    private String carrierCode;

    @JsonProperty("service_code")
    private String serviceCode;

    /** Object containing PDF, ZPL, and PNG download URLs. */
    @JsonProperty("label_download")
    private SeLabelDownload labelDownload;

    @JsonProperty("label_format")
    private String labelFormat;

    @JsonProperty("created_at")
    private String createdAt;
}
