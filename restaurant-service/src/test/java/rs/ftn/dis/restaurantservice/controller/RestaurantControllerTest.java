package rs.ftn.dis.restaurantservice.controller;

import rs.ftn.dis.restaurantservice.model.Restaurant;
import rs.ftn.dis.restaurantservice.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantController restaurantController;

    @Test
    void createRestaurant_shouldReturnSavedRestaurant() {
        Restaurant input = new Restaurant("Pizzeria Roma", "Bulevar 12");
        Restaurant saved = new Restaurant("Pizzeria Roma", "Bulevar 12");
        saved.setId(1L);

        when(restaurantRepository.save(input)).thenReturn(saved);

        ResponseEntity<Restaurant> response = restaurantController.createRestaurant(input);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getRestaurantById_whenExists_shouldReturnRestaurant() {
        Restaurant restaurant = new Restaurant("Kineski Restoran", "Ulica 5");
        restaurant.setId(3L);

        when(restaurantRepository.findById(3L)).thenReturn(Optional.of(restaurant));

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(3L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Kineski Restoran", response.getBody().getName());
    }

    @Test
    void getRestaurantById_whenNotExists_shouldReturn404() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(999L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getAllRestaurants_shouldReturnList() {
        when(restaurantRepository.findAll()).thenReturn(
                List.of(new Restaurant("A", "Adresa A"), new Restaurant("B", "Adresa B")));

        ResponseEntity<List<Restaurant>> response = restaurantController.getAllRestaurants();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }
}