package rs.ftn.dis.orderservice.service;

import rs.ftn.dis.orderservice.client.RestaurantServiceClient;
import rs.ftn.dis.orderservice.config.RabbitMQConfig;
import rs.ftn.dis.orderservice.dto.CreateOrderRequest;
import rs.ftn.dis.orderservice.dto.MenuItemDto;
import rs.ftn.dis.orderservice.dto.OrderCreatedEvent;
import rs.ftn.dis.orderservice.dto.OrderItemRequest;
import rs.ftn.dis.orderservice.model.Order;
import rs.ftn.dis.orderservice.model.OrderItem;
import rs.ftn.dis.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantServiceClient restaurantServiceClient;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RestaurantServiceClient restaurantServiceClient, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.restaurantServiceClient = restaurantServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order(request.getUserId(), request.getRestaurantId());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItemDto menuItem = restaurantServiceClient.getMenuItem(
                    request.getRestaurantId(), itemRequest.getMenuItemId());

            if (menuItem == null) {
                throw new IllegalArgumentException(
                        "Menu item " + itemRequest.getMenuItemId() + " not found in restaurant " + request.getRestaurantId());
            }

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(itemTotal);

            OrderItem orderItem = new OrderItem(
                    menuItem.getId(), menuItem.getName(), menuItem.getPrice(), itemRequest.getQuantity(), order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getUserId(), savedOrder.getRestaurantId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_CREATED_ROUTING_KEY, event);

        return savedOrder;
    }
}