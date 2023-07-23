package org.example.restaurant.service.domain;

import org.example.restaurant.service.domain.service.RestaurantDomainService;
import org.example.restaurant.service.domain.service.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }

}
