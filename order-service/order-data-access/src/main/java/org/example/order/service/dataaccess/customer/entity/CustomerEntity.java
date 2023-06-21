package org.example.order.service.dataaccess.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
@Table(name = "order_customer_m_view", schema = "customer")
@Entity
public class CustomerEntity {

    @Id
    private UUID id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
