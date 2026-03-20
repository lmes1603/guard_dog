package rycfod.com.the_tracking_guard_dog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rycfod.com.the_tracking_guard_dog.dto.ShipEngineWebhookPayload;
import rycfod.com.the_tracking_guard_dog.dto.WebhookAckResponse;
import rycfod.com.the_tracking_guard_dog.service.TrackingEventService;

/**
 * Receives inbound webhook events from ShipEngine.
 *
 * <p>Design decision: the endpoint returns 200 OK <em>immediately</em> before
 * processing the payload. ShipEngine considers any non-2xx response a failure
 * and will retry the delivery — a behaviour we want to avoid for transient
 * processing errors that are purely internal concerns.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class ShipEngineWebhookController {

    private final TrackingEventService trackingEventService;

    /**
     * POST /api/v1/webhooks/shipengine
     *
     * @param payload deserialized ShipEngine tracking event body
     * @return 200 OK with acknowledgement — always, so ShipEngine does not retry
     */
    @PostMapping("/shipengine")
    public ResponseEntity<WebhookAckResponse> receiveShipEngineEvent(
            @RequestBody ShipEngineWebhookPayload payload) {

        String resourceType = payload != null ? payload.getResourceType() : "unknown";
        log.debug("Webhook received from ShipEngine — resource_type={}", resourceType);

        trackingEventService.process(payload);

        return ResponseEntity.ok(WebhookAckResponse.of(resourceType));
    }
}
