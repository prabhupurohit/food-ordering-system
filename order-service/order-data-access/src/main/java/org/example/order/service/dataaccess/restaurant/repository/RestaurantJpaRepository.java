package org.example.order.service.dataaccess.restaurant.repository;

import org.example.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import org.example.order.service.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    //This will be converted to a SQL query with IN Statement
    Optional<List<RestaurantEntity>> findRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);

}
