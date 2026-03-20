package rycfod.com.the_tracking_guard_dog.dto.label;

import lombok.Builder;
import lombok.Data;

/**
 * Simplified response returned by {@code POST /api/v1/labels/create}.
 * Exposes only what the dropshipping workflow needs; raw ShipEngine fields
 * stay in the {@code dto.shipengine} package.
 */
@Data
@Builder
public class LabelCreatedResponse {

    private String labelId;
    private String shipmentId;
    private String trackingNumber;
    private String carrierCode;
    private String serviceCode;
    private String status;

    /** Direct URL to download the PDF label. */
    private String labelDownloadUrl;

    /** ZPL version for thermal printers (warehouses). */
    private String labelDownloadZpl;
}
