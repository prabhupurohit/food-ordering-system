package org.example.order.service.dataaccess.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
//Spring will need the No Args Constructor to create a Proxy Class
@NoArgsConstructor
@AllArgsConstructor
//Use a Materialized View from Customer DB Schema in the Order Service
// Later will be replaced with CQRS pattern
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@IdClass(RestaurantEntityId.class)
@Entity
public class RestaurantEntity {

    @Id
    private UUID restaurantId;
    @Id
    private UUID productId;

    private String restaurantName;
    private boolean restaurantActive;
    private String productName;
    private BigDecimal productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, productId);
    }
}
