package rycfod.com.the_tracking_guard_dog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rycfod.com.the_tracking_guard_dog.dto.label.CreateLabelRequest;
import rycfod.com.the_tracking_guard_dog.dto.label.LabelCreatedResponse;
import rycfod.com.the_tracking_guard_dog.service.LabelService;

/**
 * Exposes the label-creation surface of the Tracking Guard Dog API.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    /**
     * POST /api/v1/labels/create
     *
     * <p>Creates a shipping label via ShipEngine. Supports two modes:</p>
     * <ul>
     *   <li><b>Mode A</b> — provide {@code shipment_id} only.</li>
     *   <li><b>Mode B</b> — provide {@code carrier_id}, {@code service_code},
     *       {@code ship_to}, {@code ship_from}, and {@code packages}.</li>
     * </ul>
     *
     * @param request label creation parameters
     * @return 201 Created with label ID, tracking number, and download URLs
     */
    @PostMapping("/create")
    public ResponseEntity<LabelCreatedResponse> createLabel(
            @Valid @RequestBody CreateLabelRequest request) {

        log.info("[Label] Creation request — shipment_id={} carrier_id={}",
                request.getShipmentId(), request.getCarrierId());

        LabelCreatedResponse response = labelService.createLabel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
