package org.example.order.service.domain.mapper;

import org.example.domain.valueobject.CustomerId;
import org.example.domain.valueobject.Money;
import org.example.domain.valueobject.ProductId;
import org.example.domain.valueobject.RestaurantId;
import org.example.order.service.domain.dto.create.CreateOrderCommand;
import org.example.order.service.domain.dto.create.CreateOrderResponse;
import org.example.order.service.domain.dto.create.OrderAddress;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.OrderItem;
import org.example.order.service.domain.entity.Product;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//This is the Factory which creates Domain entities from the input parameters
@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream()
                        .map(orderItem -> new Product(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<org.example.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .trackingId(order.getTrackingId().getValue())
                //.message() -> Should Response message be set here?
                .build();
    }
}
