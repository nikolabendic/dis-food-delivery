package rs.ftn.dis.deliveryservice.controller;

import rs.ftn.dis.deliveryservice.model.Delivery;
import rs.ftn.dis.deliveryservice.repository.DeliveryRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class DeliveryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    void setUp() {
        deliveryRepository.deleteAll();
    }

    @Test
    void getAllDeliveries_shouldReturnFromDatabase() {
        deliveryRepository.save(new Delivery(1L, "Petar"));
        deliveryRepository.save(new Delivery(2L, "Jovana"));

        ResponseEntity<Delivery[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/deliveries", Delivery[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void getByOrderId_shouldReturnCorrectDelivery() {
        deliveryRepository.save(new Delivery(10L, "Stefan"));

        ResponseEntity<Delivery> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/deliveries/order/10", Delivery.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Stefan", response.getBody().getCourierName());
    }
}