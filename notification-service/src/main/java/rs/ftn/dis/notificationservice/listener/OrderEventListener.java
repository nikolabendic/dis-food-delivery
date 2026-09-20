package rs.ftn.dis.notificationservice.listener;

import rs.ftn.dis.notificationservice.config.RabbitMQConfig;
import rs.ftn.dis.notificationservice.dto.OrderCreatedEvent;
import rs.ftn.dis.notificationservice.model.Notification;
import rs.ftn.dis.notificationservice.store.NotificationStore;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final NotificationStore notificationStore;

    public OrderEventListener(NotificationStore notificationStore) {
        this.notificationStore = notificationStore;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        String message = "Vaša porudžbina #" + event.getOrderId() + " je uspešno kreirana!";

        Notification notification = new Notification(event.getOrderId(), event.getUserId(), message);
        notificationStore.add(notification);

        System.out.println("[NOTIFICATION] Korisniku " + event.getUserId() + ": " + message);
    }
}