package org.example.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.example.order.service.domain.dto.create.CreateOrderCommand;
import org.example.order.service.domain.dto.create.CreateOrderResponse;
import org.example.order.service.domain.event.OrderCreatedEvent;
import org.example.order.service.domain.mapper.OrderDataMapper;
import org.example.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(OrderCreateHelper orderCreateHelper, OrderDataMapper orderDataMapper,
                                     OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    //@Transactional
    //As the persistOrder() has been made Transactional, no need to annotate this method with @Transactional
    //Also persistOrder() has to be part of another bin (OrderCreateHelper in this case) for the @Transactional to work
    //If persistOrder() is a local method annotated with @Transactional, it will not create a transaction
    //As Spring AOP creates Proxy around the Beans for processing AOP annotations like @Transactional,
    //The method has to be called though the proxy which means through the separate Bean (OrderCreateHelper in this case)

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }


}
