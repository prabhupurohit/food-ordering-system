package org.example.order.service.application.rest;

import lombok.extern.slf4j.Slf4j;
import org.example.order.service.domain.dto.create.CreateOrderCommand;
import org.example.order.service.domain.dto.create.CreateOrderResponse;
import org.example.order.service.domain.dto.track.TrackOrderQuery;
import org.example.order.service.domain.dto.track.TrackOrderResponse;
import org.example.order.service.domain.ports.input.service.OrderApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value="/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrderResponseResponseEntity(@RequestBody CreateOrderCommand  createOrderCommand) {
        log.info("Creating order for customer: {} at restaurant {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", createOrderResponse.getTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService
                .trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderResponse);
    }

}
