package rycfod.com.the_tracking_guard_dog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory cooldown guard: prevents the same tracking exception from
 * triggering more than one WhatsApp alert per {@link #COOLDOWN} window.
 *
 * <p>Key = {@code trackingNumber:statusCode}. State is lost on restart,
 * which is acceptable — a restart clears the window deliberately.</p>
 */
@Slf4j
@Service
public class TrackingCooldownService {

    static final Duration COOLDOWN = Duration.ofHours(1);

    private final ConcurrentHashMap<String, Instant> lastSent = new ConcurrentHashMap<>();

    /**
     * Returns {@code true} if enough time has passed since the last
     * notification for this tracking + status combination.
     */
    public boolean shouldNotify(String trackingNumber, String statusCode) {
        String key = key(trackingNumber, statusCode);
        Instant last = lastSent.get(key);
        if (last == null) return true;

        boolean expired = Instant.now().isAfter(last.plus(COOLDOWN));
        if (!expired) {
            log.info("[COOLDOWN] Suppressing duplicate alert — tracking_number={} status={} next_allowed={}",
                    trackingNumber, statusCode, last.plus(COOLDOWN));
        }
        return expired;
    }

    /** Records that a notification was successfully sent for this combination. */
    public void markSent(String trackingNumber, String statusCode) {
        lastSent.put(key(trackingNumber, statusCode), Instant.now());
    }

    private static String key(String trackingNumber, String statusCode) {
        return trackingNumber + ":" + statusCode;
    }
}
