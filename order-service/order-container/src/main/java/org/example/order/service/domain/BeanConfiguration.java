package org.example.order.service.domain;

import org.example.order.service.domain.service.OrderDomainService;
import org.example.order.service.domain.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
