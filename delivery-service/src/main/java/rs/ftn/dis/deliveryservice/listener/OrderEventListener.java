package rs.ftn.dis.deliveryservice.listener;

import rs.ftn.dis.deliveryservice.config.RabbitMQConfig;
import rs.ftn.dis.deliveryservice.dto.OrderCreatedEvent;
import rs.ftn.dis.deliveryservice.model.Delivery;
import rs.ftn.dis.deliveryservice.repository.DeliveryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class OrderEventListener {

    private final DeliveryRepository deliveryRepository;
    private static final List<String> COURIERS = List.of("Marko", "Ana", "Petar", "Jovana");

    public OrderEventListener(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.DELIVERY_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        String assignedCourier = COURIERS.get(new Random().nextInt(COURIERS.size()));

        Delivery delivery = new Delivery(event.getOrderId(), assignedCourier);
        deliveryRepository.save(delivery);

        System.out.println("Dodeljen kurir '" + assignedCourier + "' za porudžbinu #" + event.getOrderId());
    }
}