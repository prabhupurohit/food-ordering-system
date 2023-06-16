package org.example.order.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.valueobject.OrderStatus;
import org.example.order.service.domain.dto.track.TrackOrderQuery;
import org.example.order.service.domain.dto.track.TrackOrderResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class OrderTrackCommandHandler {
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return new TrackOrderResponse(
                UUID.randomUUID(),
                OrderStatus.PENDING,
                null
        );
    }
}
