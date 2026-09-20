package rs.ftn.dis.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuItemId;

    @Column(nullable = false)
    private String menuItemName;

    @Column(nullable = false)
    private BigDecimal priceAtOrderTime;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    public OrderItem() {}

    public OrderItem(Long menuItemId, String menuItemName, BigDecimal priceAtOrderTime, Integer quantity, Order order) {
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.priceAtOrderTime = priceAtOrderTime;
        this.quantity = quantity;
        this.order = order;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    public String getMenuItemName() { return menuItemName; }
    public void setMenuItemName(String menuItemName) { this.menuItemName = menuItemName; }
    public BigDecimal getPriceAtOrderTime() { return priceAtOrderTime; }
    public void setPriceAtOrderTime(BigDecimal priceAtOrderTime) { this.priceAtOrderTime = priceAtOrderTime; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}