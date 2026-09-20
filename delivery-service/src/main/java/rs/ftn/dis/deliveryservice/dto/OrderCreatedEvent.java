package rs.ftn.dis.deliveryservice.dto;

public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private Long restaurantId;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(Long orderId, Long userId, Long restaurantId) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}