package rycfod.com.the_tracking_guard_dog.model;

/**
 * Status codes as defined by ShipEngine's tracking API.
 * Reference: https://www.shipengine.com/docs/tracking/
 */
public enum TrackingStatusCode {

    // Normal flow
    UN,  // Unknown
    AC,  // Accepted / In Transit
    IT,  // In Transit
    DE,  // Delivered

    // Exception states — trigger critical alert
    AT,  // Attempted Delivery
    EX,  // Exception
    NY,  // Not Yet In System

    // Other
    UN_KNOWN; // Fallback for unrecognized codes

    public static TrackingStatusCode fromCode(String code) {
        if (code == null) return UN_KNOWN;
        try {
            return valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UN_KNOWN;
        }
    }

    /**
     * Returns true if this status represents a delivery problem
     * that requires immediate attention.
     */
    public boolean isException() {
        return this == EX || this == AT;
    }
}
