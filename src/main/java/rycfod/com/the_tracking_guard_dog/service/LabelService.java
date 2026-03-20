package rycfod.com.the_tracking_guard_dog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rycfod.com.the_tracking_guard_dog.client.ShipEngineClient;
import rycfod.com.the_tracking_guard_dog.dto.label.CreateLabelRequest;
import rycfod.com.the_tracking_guard_dog.dto.label.LabelCreatedResponse;
import rycfod.com.the_tracking_guard_dog.dto.shipengine.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Orchestrates label creation.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Validate/choose the correct request mode (existing shipment vs inline).</li>
 *   <li>Map our internal DTOs → ShipEngine wire format via
 *       {@link ShipEngineClient}.</li>
 *   <li>Map the ShipEngine response → our {@link LabelCreatedResponse}.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelService {

    private final ShipEngineClient shipEngineClient;

    /**
     * Creates a label for the given request.
     *
     * <p>If {@code shipmentId} is present, uses Mode A (existing shipment).
     * Otherwise, builds an inline shipment from the address and package fields.</p>
     *
     * @param request our internal label creation request
     * @return simplified label response for the caller
     */
    public LabelCreatedResponse createLabel(CreateLabelRequest request) {
        SeLabelRequest seRequest = buildSeRequest(request);
        SeLabelResponse seResponse = shipEngineClient.createLabel(seRequest);
        return mapToResponse(seResponse);
    }

    // ------------------------------------------------------------------
    // Private mapping helpers
    // ------------------------------------------------------------------

    private SeLabelRequest buildSeRequest(CreateLabelRequest req) {
        SeLabelRequest.SeLabelRequestBuilder builder = SeLabelRequest.builder()
                .labelFormat(req.getLabelFormat());

        if (req.getShipmentId() != null && !req.getShipmentId().isBlank()) {
            // Mode A — just reference the pre-existing shipment
            log.debug("[Label] Mode A: using existing shipment_id={}", req.getShipmentId());
            builder.shipmentId(req.getShipmentId());
        } else {
            // Mode B — build shipment inline
            log.debug("[Label] Mode B: building inline shipment for carrier_id={}", req.getCarrierId());
            builder.shipment(buildSeShipment(req));
        }

        return builder.build();
    }

    private SeShipment buildSeShipment(CreateLabelRequest req) {
        return SeShipment.builder()
                .carrierId(req.getCarrierId())
                .serviceCode(req.getServiceCode())
                .shipTo(mapAddress(req.getShipTo()))
                .shipFrom(mapAddress(req.getShipFrom()))
                .packages(mapPackages(req.getPackages()))
                .build();
    }

    private SeAddress mapAddress(rycfod.com.the_tracking_guard_dog.dto.label.AddressDto dto) {
        if (dto == null) return null;
        return SeAddress.builder()
                .name(dto.getName())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .cityLocality(dto.getCityLocality())
                .stateProvince(dto.getStateProvince())
                .postalCode(dto.getPostalCode())
                .countryCode(dto.getCountryCode())
                .addressResidentialIndicator(dto.getAddressResidentialIndicator())
                .phone(dto.getPhone())
                .build();
    }

    private List<SePackage> mapPackages(
            List<rycfod.com.the_tracking_guard_dog.dto.label.PackageDto> packages) {
        if (packages == null) return List.of();
        return packages.stream()
                .map(p -> SePackage.builder()
                        .weight(SeWeight.builder()
                                .value(p.getWeightValue())
                                .unit(p.getWeightUnit())
                                .build())
                        .dimensions(SeDimensions.builder()
                                .length(p.getLength())
                                .width(p.getWidth())
                                .height(p.getHeight())
                                .unit(p.getDimensionUnit())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    private LabelCreatedResponse mapToResponse(SeLabelResponse se) {
        String downloadUrl = se.getLabelDownload() != null ? se.getLabelDownload().getPdf()  : null;
        String zplUrl      = se.getLabelDownload() != null ? se.getLabelDownload().getZpl()  : null;

        return LabelCreatedResponse.builder()
                .labelId(se.getLabelId())
                .shipmentId(se.getShipmentId())
                .trackingNumber(se.getTrackingNumber())
                .carrierCode(se.getCarrierCode())
                .serviceCode(se.getServiceCode())
                .status(se.getStatus())
                .labelDownloadUrl(downloadUrl)
                .labelDownloadZpl(zplUrl)
                .build();
    }
}
