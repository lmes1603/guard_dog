package rycfod.com.the_tracking_guard_dog.dto.label;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request body for {@code POST /api/v1/labels/create}.
 *
 * <h3>Two usage modes:</h3>
 * <ol>
 *   <li><b>Existing shipment</b>: provide only {@code shipmentId}.
 *       All other fields are ignored.</li>
 *   <li><b>Inline shipment</b>: leave {@code shipmentId} null and fill in
 *       {@code carrierId}, {@code serviceCode}, {@code shipTo}, {@code shipFrom},
 *       and {@code packages}.</li>
 * </ol>
 */
@Data
@NoArgsConstructor
public class CreateLabelRequest {

    // ── Mode A: existing shipment ──────────────────────────────────────────
    private String shipmentId;

    // ── Mode B: inline shipment ────────────────────────────────────────────
    /** ShipEngine carrier account ID, e.g. "se-123456". */
    private String carrierId;

    /** Service code, e.g. "usps_priority_mail", "fedex_ground". */
    private String serviceCode;

    @Valid
    private AddressDto shipTo;

    @Valid
    private AddressDto shipFrom;

    @Valid
    private List<PackageDto> packages;

    // ── Label options ──────────────────────────────────────────────────────
    /** "pdf" (default), "zpl", or "png". */
    private String labelFormat = "pdf";
}
