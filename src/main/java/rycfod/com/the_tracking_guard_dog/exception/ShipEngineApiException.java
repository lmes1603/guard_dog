package rycfod.com.the_tracking_guard_dog.exception;

import org.springframework.http.HttpStatusCode;

/**
 * Wraps any non-2xx response received from the ShipEngine API.
 * Carries the original HTTP status so the global handler can propagate it.
 */
public class ShipEngineApiException extends RuntimeException {

    private final HttpStatusCode httpStatus;

    public ShipEngineApiException(HttpStatusCode httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }
}
