package org.example.order.service.domain.service;

import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.event.OrderCancelledEvent;
import org.example.order.service.domain.event.OrderCreatedEvent;
import org.example.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    //The Events returned from the domain layer can be fired in the Application Service
    //Domain layer should have no knowledge of where the Events are being fired

    //The following method is a perfect candidate for Domain service because
    //it deals with 2 different entities or aggregates.
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    //The following methods however can be part of the Order entity class.
    //Keeping it in the Domain Service provides a clean design because Application Service will rely only on Domain Service.
    OrderPaidEvent payOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    //The following methods can also create events, but as this is a terminal operation, for simplicity, it's not included
    void approveOrder(Order order);
    void cancelOrder(Order order, List<String> failureMessages);
}
