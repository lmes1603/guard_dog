package rycfod.com.the_tracking_guard_dog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Audit record written every time a WhatsApp alert is successfully delivered.
 * Stored in the H2 in-memory database; useful for cost analysis (one record = one Twilio message).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trackingNumber;

    @Column(nullable = false)
    private String statusCode;

    /** The Message SID returned by Twilio — use this to look up cost in the Twilio Console. */
    @Column(nullable = false)
    private String messageSid;

    @Column(nullable = false)
    private Instant sentAt;
}
