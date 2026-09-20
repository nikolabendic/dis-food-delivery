package rs.ftn.dis.deliveryservice.repository;

import rs.ftn.dis.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}