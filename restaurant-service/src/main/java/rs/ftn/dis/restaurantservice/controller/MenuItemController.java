package rs.ftn.dis.restaurantservice.controller;

import rs.ftn.dis.restaurantservice.model.MenuItem;
import rs.ftn.dis.restaurantservice.model.Restaurant;
import rs.ftn.dis.restaurantservice.repository.MenuItemRepository;
import rs.ftn.dis.restaurantservice.repository.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemController(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@PathVariable Long restaurantId, @RequestBody MenuItem menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        menuItem.setRestaurant(restaurant);
        return ResponseEntity.ok(menuItemRepository.save(menuItem));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemRepository.findByRestaurantId(restaurantId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long restaurantId, @PathVariable Long itemId) {
        return menuItemRepository.findById(itemId)
                .filter(item -> item.getRestaurant().getId().equals(restaurantId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}