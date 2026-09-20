package rs.ftn.dis.deliveryservice.controller;

import rs.ftn.dis.deliveryservice.model.Delivery;
import rs.ftn.dis.deliveryservice.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryController deliveryController;

    @Test
    void getAllDeliveries_shouldReturnList() {
        Delivery delivery = new Delivery(1L, "Marko");
        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));

        ResponseEntity<List<Delivery>> response = deliveryController.getAllDeliveries();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getByOrderId_whenExists_shouldReturnDelivery() {
        Delivery delivery = new Delivery(5L, "Ana");
        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));

        ResponseEntity<Delivery> response = deliveryController.getByOrderId(5L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Ana", response.getBody().getCourierName());
    }

    @Test
    void getByOrderId_whenNotExists_shouldReturn404() {
        when(deliveryRepository.findAll()).thenReturn(List.of());

        ResponseEntity<Delivery> response = deliveryController.getByOrderId(999L);

        assertEquals(404, response.getStatusCode().value());
    }
}