package org.example.order.service.dataaccess.restaurant.adapter;

import org.example.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import org.example.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import org.example.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProductIds = restaurantDataAccessMapper.restaurantToRestaurantProductIds(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProductIds);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
