package org.example.order.service.messaging.mapper;

import org.example.kafka.order.avro.model.*;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.event.OrderCancelledEvent;
import org.example.order.service.domain.event.OrderCreatedEvent;
import org.example.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel
    orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {

        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setOrderId(order.getId().getValue().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel
    orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {

        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setOrderId(order.getId().getValue().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();

    }

    public RestaurantApprovalRequestAvroModel
    orderPaidEventToRestaurantApprovalRequestAvroModel (OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getOrderItems().stream().map(orderItem ->
                        Product.newBuilder()
                            .setId(orderItem.getOrderId().getValue().toString())
                            .setQuantity(orderItem.getQuantity())
                            .build()).collect(Collectors.toList()))
                .setPrice(order.getPrice().getAmount())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }
}
