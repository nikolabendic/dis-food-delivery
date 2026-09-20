package rs.ftn.dis.orderservice.service;

import rs.ftn.dis.orderservice.client.RestaurantServiceClient;
import rs.ftn.dis.orderservice.config.RabbitMQConfig;
import rs.ftn.dis.orderservice.dto.CreateOrderRequest;
import rs.ftn.dis.orderservice.dto.MenuItemDto;
import rs.ftn.dis.orderservice.dto.OrderItemRequest;
import rs.ftn.dis.orderservice.model.Order;
import rs.ftn.dis.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantServiceClient restaurantServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldCalculateTotalPriceCorrectly() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setRestaurantId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(2);
        request.setItems(List.of(itemRequest));

        MenuItemDto menuItem = new MenuItemDto();
        menuItem.setId(1L);
        menuItem.setName("Margarita");
        menuItem.setPrice(BigDecimal.valueOf(650));

        when(restaurantServiceClient.getMenuItem(1L, 1L)).thenReturn(menuItem);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });

        Order result = orderService.createOrder(request);

        assertEquals(BigDecimal.valueOf(1300), result.getTotalPrice());
        assertEquals(100L, result.getId());
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE), eq(RabbitMQConfig.ORDER_CREATED_ROUTING_KEY), any(Object.class));
    }

    @Test
    void createOrder_whenMenuItemNotFound_shouldThrowException() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setRestaurantId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(999L);
        itemRequest.setQuantity(1);
        request.setItems(List.of(itemRequest));

        when(restaurantServiceClient.getMenuItem(1L, 999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
    }
}