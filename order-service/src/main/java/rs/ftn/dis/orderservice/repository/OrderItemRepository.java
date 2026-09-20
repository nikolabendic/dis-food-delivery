package rs.ftn.dis.orderservice.repository;

import rs.ftn.dis.orderservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}