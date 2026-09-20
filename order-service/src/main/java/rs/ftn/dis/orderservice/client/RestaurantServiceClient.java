package rs.ftn.dis.orderservice.client;

import rs.ftn.dis.orderservice.dto.MenuItemDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestaurantServiceClient {

    private final RestClient restClient;

    public RestaurantServiceClient(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("http://restaurant-service").build();
    }

    public MenuItemDto getMenuItem(Long restaurantId, Long menuItemId) {
        return restClient.get()
                .uri("/api/restaurants/{restaurantId}/menu-items/{itemId}", restaurantId, menuItemId)
                .retrieve()
                .body(MenuItemDto.class);
    }
}