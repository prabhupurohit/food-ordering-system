package org.example.order.service.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.event.publisher.DomainEventPublisher;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Product;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.event.OrderCancelledEvent;
import org.example.order.service.domain.event.OrderCreatedEvent;
import org.example.order.service.domain.event.OrderPaidEvent;
import org.example.order.service.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.example.domain.DomainConstants.UTC;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant,
                                                      DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher) {
        //This can be a method inside the Restaurant Aggregate
        //TODO: Will refactor later
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        //At the time of validating order, OrderId and OrderStatus should be null
        order.validateOrder();
        //The OrderId and OrderStatus is set in the initializeOrder() method
        order.initializeOrder();
        log.info("Order with id {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventDomainEventPublisher);
    }

    //TODO: The following method has N^2 time complexity. The Restaurant can store the products in a Hashmap to reduce time complexity
    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems()
                .forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
                    Product currentProduct = orderItem.getProduct();
                    if (currentProduct.equals(restaurantProduct)) {
                        currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                                restaurantProduct.getPrice());
                    }
                }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventDomainEventPublisher);
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,
                                                  DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCancelledEventDomainEventPublisher);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());

    }
}
