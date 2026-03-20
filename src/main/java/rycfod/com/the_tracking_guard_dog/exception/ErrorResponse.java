package rycfod.com.the_tracking_guard_dog.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/** Standard error envelope returned to the caller on any failure. */
@Data
@Builder
public class ErrorResponse {

    private int    status;
    private String error;
    private String message;
    private String path;

    @Builder.Default
    private Instant timestamp = Instant.now();
}
