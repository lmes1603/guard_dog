package rycfod.com.the_tracking_guard_dog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.exception.ApiException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rycfod.com.the_tracking_guard_dog.model.NotificationLog;
import rycfod.com.the_tracking_guard_dog.repository.NotificationLogRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Sends WhatsApp alerts via a Twilio approved Content Template.
 *
 * <p>Template variables:
 * <ul>
 *   <li>{{1}} — tracking number</li>
 *   <li>{{2}} — exception / problem description</li>
 *   <li>{{3}} — carrier status description</li>
 * </ul>
 * The message body must be empty when using ContentSid; Twilio renders it from the template.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwilioService {

    private static final String NA = "Información no proporcionada por el transportista";

    private final NotificationLogRepository notificationLogRepository;
    private final TrackingCooldownService cooldownService;
    private final ObjectMapper objectMapper;

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @Value("${twilio.to-number}")
    private String toNumber;

    @Value("${twilio.content-sid}")
    private String contentSid;

    private TwilioRestClient twilioClient;

    @PostConstruct
    void init() {
        twilioClient = new TwilioRestClient.Builder(accountSid, authToken).build();
        log.info("[TWILIO] REST client ready — account={} contentSid={}", accountSid, contentSid);
    }

    /**
     * Sends a WhatsApp alert using the approved Content Template.
     * Called from a background thread via {@link java.util.concurrent.CompletableFuture#runAsync}.
     */
    public void sendWhatsAppAlert(String trackingNumber,
                                  String statusCode,
                                  String exceptionDescription,
                                  String carrierStatusDescription) {
        try {
            String contentVariables = buildContentVariables(
                    trackingNumber, exceptionDescription, carrierStatusDescription);

            Message message = Message.creator(
                            new PhoneNumber(toNumber),
                            new PhoneNumber(fromNumber),
                            "")                          // body vacío — Twilio usa el template
                    .setContentSid(contentSid)
                    .setContentVariables(contentVariables)
                    .create(twilioClient);

            log.info("[TWILIO] WhatsApp alert sent — sid={} tracking_number={}",
                    message.getSid(), trackingNumber);

            notificationLogRepository.save(NotificationLog.builder()
                    .trackingNumber(trackingNumber)
                    .statusCode(statusCode)
                    .messageSid(message.getSid())
                    .sentAt(Instant.now())
                    .build());

            cooldownService.markSent(trackingNumber, statusCode);

        } catch (ApiException e) {
            log.error("[TWILIO] Failed to send WhatsApp alert — tracking_number={} code={} message={}",
                    trackingNumber, e.getCode(), e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("[TWILIO] Failed to serialize content variables — tracking_number={}: {}",
                    trackingNumber, e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    /**
     * Builds the JSON object required by Twilio's {@code ContentVariables} field.
     * Keys match the template placeholders: "1" → {{1}}, "2" → {{2}}, "3" → {{3}}.
     */
    private String buildContentVariables(String trackingNumber,
                                         String exceptionDescription,
                                         String carrierStatusDescription) throws JsonProcessingException {

        String problema = Optional.ofNullable(exceptionDescription)
                .filter(s -> !s.isBlank())
                .orElse(NA);

        String carrier = Optional.ofNullable(carrierStatusDescription)
                .filter(s -> !s.isBlank())
                .orElse(NA);

        return objectMapper.writeValueAsString(Map.of(
                "1", trackingNumber,
                "2", problema,
                "3", carrier
        ));
    }
}
