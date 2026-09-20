package rs.ftn.dis.restaurantservice.repository;

import rs.ftn.dis.restaurantservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}