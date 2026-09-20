package rs.ftn.dis.orderservice.controller;

import rs.ftn.dis.orderservice.client.RestaurantServiceClient;
import rs.ftn.dis.orderservice.dto.CreateOrderRequest;
import rs.ftn.dis.orderservice.dto.MenuItemDto;
import rs.ftn.dis.orderservice.dto.OrderItemRequest;
import rs.ftn.dis.orderservice.model.Order;
import rs.ftn.dis.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private RestaurantServiceClient restaurantServiceClient;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void createOrder_shouldPersistOrderWithCorrectTotal() {
        MenuItemDto menuItem = new MenuItemDto();
        menuItem.setId(1L);
        menuItem.setName("Margarita");
        menuItem.setPrice(BigDecimal.valueOf(650));
        when(restaurantServiceClient.getMenuItem(1L, 1L)).thenReturn(menuItem);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setRestaurantId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1L);
        itemRequest.setQuantity(3);
        request.setItems(List.of(itemRequest));

        ResponseEntity<Order> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/orders", request, Order.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(0, BigDecimal.valueOf(1950).compareTo(response.getBody().getTotalPrice()));
        assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    void createOrder_whenMenuItemNotFound_shouldReturnBadRequest() {
        when(restaurantServiceClient.getMenuItem(1L, 999L)).thenReturn(null);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setRestaurantId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(999L);
        itemRequest.setQuantity(1);
        request.setItems(List.of(itemRequest));

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/orders", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}