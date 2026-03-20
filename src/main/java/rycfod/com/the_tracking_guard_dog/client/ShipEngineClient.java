package rycfod.com.the_tracking_guard_dog.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import rycfod.com.the_tracking_guard_dog.config.ShipEngineProperties;
import rycfod.com.the_tracking_guard_dog.dto.shipengine.SeLabelRequest;
import rycfod.com.the_tracking_guard_dog.dto.shipengine.SeLabelResponse;
import rycfod.com.the_tracking_guard_dog.exception.ShipEngineApiException;

/**
 * Low-level HTTP adapter for the ShipEngine REST API.
 *
 * <p>Single Responsibility: knows <em>how</em> to talk to ShipEngine — URI
 * construction, serialisation, and error translation. Business logic lives
 * in {@link rycfod.com.the_tracking_guard_dog.service.LabelService}.</p>
 *
 * <p>The {@code API-Key} header is injected transparently by the interceptor
 * registered in {@link rycfod.com.the_tracking_guard_dog.config.AppConfig}.</p>
 */
@Slf4j
@Component
public class ShipEngineClient {

    private static final String LABELS_ENDPOINT = "/v1/labels";

    private final RestTemplate        restTemplate;
    private final ShipEngineProperties props;

    public ShipEngineClient(
            @Qualifier("shipEngineRestTemplate") RestTemplate restTemplate,
            ShipEngineProperties props) {
        this.restTemplate = restTemplate;
        this.props        = props;
    }

    /**
     * Creates a shipping label by calling {@code POST /v1/labels}.
     *
     * @param request the fully-built ShipEngine label request body
     * @return the ShipEngine label response
     * @throws ShipEngineApiException if ShipEngine returns any non-2xx status
     */
    public SeLabelResponse createLabel(SeLabelRequest request) {
        String url = props.getBaseUrl() + LABELS_ENDPOINT;
        log.info("[ShipEngine] POST {} — shipment_id={}", url, request.getShipmentId());

        try {
            ResponseEntity<SeLabelResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    SeLabelResponse.class
            );

            SeLabelResponse body = response.getBody();
            log.info("[ShipEngine] Label created — label_id={} tracking={}",
                    body != null ? body.getLabelId() : "null",
                    body != null ? body.getTrackingNumber() : "null");
            return body;

        } catch (HttpClientErrorException ex) {
            // 4xx — bad request, invalid API key, rate limit, etc.
            String detail = extractErrorMessage(ex.getResponseBodyAsString(), ex.getMessage());
            log.warn("[ShipEngine] Client error {} — {}", ex.getStatusCode(), detail);
            throw new ShipEngineApiException(ex.getStatusCode(),
                    "ShipEngine rejected the request: " + detail);

        } catch (HttpServerErrorException ex) {
            // 5xx — ShipEngine is having issues
            log.error("[ShipEngine] Server error {} — {}", ex.getStatusCode(), ex.getMessage());
            throw new ShipEngineApiException(ex.getStatusCode(),
                    "ShipEngine is temporarily unavailable. Please retry later.");
        }
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    /**
     * Tries to surface the most useful error text from the response body.
     * ShipEngine wraps errors in {"errors":[{"message":"..."}]}.
     */
    private String extractErrorMessage(String responseBody, String fallback) {
        if (responseBody != null && !responseBody.isBlank()) {
            // Return the raw body so the caller sees the full ShipEngine error.
            // A future iteration can deserialise this into a proper error DTO.
            return responseBody;
        }
        return fallback;
    }
}
