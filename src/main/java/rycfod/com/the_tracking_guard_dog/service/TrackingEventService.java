package rycfod.com.the_tracking_guard_dog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rycfod.com.the_tracking_guard_dog.dto.ShipEngineTrackingData;
import rycfod.com.the_tracking_guard_dog.dto.ShipEngineWebhookPayload;
import rycfod.com.the_tracking_guard_dog.model.TrackingStatusCode;

import java.util.concurrent.CompletableFuture;

/**
 * Core service responsible for processing ShipEngine webhook events.
 *
 * <p>Single Responsibility: decides what to do with each inbound tracking event.
 * Future expansions (DB persistence, notifications) should be injected as
 * collaborators rather than added directly here.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingEventService {

    private final TwilioService twilioService;
    private final TrackingCooldownService cooldownService;

    /**
     * Entry point for every inbound webhook event.
     *
     * @param payload the deserialized ShipEngine webhook body
     */
    public void process(ShipEngineWebhookPayload payload) {
        if (payload == null || payload.getData() == null) {
            log.warn("Received an empty or malformed ShipEngine webhook payload — skipping.");
            return;
        }

        ShipEngineTrackingData data = payload.getData();
        TrackingStatusCode status = TrackingStatusCode.fromCode(data.getStatusCode());

        log.info("[TRACKING] number={} status={} description=\"{}\"",
                data.getTrackingNumber(),
                status,
                data.getStatusDescription());

        if (status.isException()) {
            handleException(data, status);
        } else {
            log.debug("[TRACKING] Normal event — no action required. carrier_status=\"{}\"",
                    data.getCarrierStatusDescription());
        }
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    private void handleException(ShipEngineTrackingData data, TrackingStatusCode status) {
        log.error("""
                ╔══════════════════════════════════════════════════════╗
                ║          ⚠  TRACKING EXCEPTION DETECTED  ⚠          ║
                ╠══════════════════════════════════════════════════════╣
                ║  tracking_number        : {}
                ║  status_code            : {}
                ║  status_description     : {}
                ║  carrier_status_desc    : {}
                ║  exception_description  : {}
                ╚══════════════════════════════════════════════════════╝""",
                data.getTrackingNumber(),
                status,
                data.getStatusDescription(),
                data.getCarrierStatusDescription(),
                data.getExceptionDescription());

        String statusName = status.name();

        if (!cooldownService.shouldNotify(data.getTrackingNumber(), statusName)) {
            return;
        }

        CompletableFuture.runAsync(() -> twilioService.sendWhatsAppAlert(
                data.getTrackingNumber(),
                statusName,
                data.getExceptionDescription(),
                data.getCarrierStatusDescription()
        ));
    }
}
