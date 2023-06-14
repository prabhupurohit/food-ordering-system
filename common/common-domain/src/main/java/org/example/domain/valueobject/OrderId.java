package org.example.domain.valueobject;

import java.util.UUID;

//This is created inside common package because this will be used across the system
public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }
}
