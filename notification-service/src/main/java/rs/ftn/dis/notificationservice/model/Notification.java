package rs.ftn.dis.notificationservice.model;

import java.time.LocalDateTime;

public class Notification {

    private Long orderId;
    private Long userId;
    private String message;
    private LocalDateTime sentAt;

    public Notification(Long orderId, Long userId, String message) {
        this.orderId = orderId;
        this.userId = userId;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public String getMessage() { return message; }
    public LocalDateTime getSentAt() { return sentAt; }
}