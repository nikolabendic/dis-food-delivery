package rs.ftn.dis.restaurantservice.controller;

import rs.ftn.dis.restaurantservice.model.Restaurant;
import rs.ftn.dis.restaurantservice.repository.RestaurantRepository;
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
class RestaurantControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
    }

    @Test
    void createRestaurant_shouldPersistInDatabase() {
        Restaurant newRestaurant = new Restaurant("Sushi Bar", "Trg 3");

        ResponseEntity<Restaurant> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/restaurants", newRestaurant, Restaurant.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(1, restaurantRepository.findAll().size());
    }

    @Test
    void getAllRestaurants_shouldReturnFromDatabase() {
        restaurantRepository.save(new Restaurant("A", "Adresa A"));
        restaurantRepository.save(new Restaurant("B", "Adresa B"));

        ResponseEntity<Restaurant[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/restaurants", Restaurant[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void getRestaurantById_whenNotExists_shouldReturn404() {
        ResponseEntity<Restaurant> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/restaurants/12345", Restaurant.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}