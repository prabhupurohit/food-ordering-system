package org.example.restaurant.service.domain.entity;

import org.example.domain.entity.AggregateRoot;
import org.example.domain.valueobject.Money;
import org.example.domain.valueobject.OrderApprovalStatus;
import org.example.domain.valueobject.OrderStatus;
import org.example.domain.valueobject.RestaurantId;
import org.example.restaurant.service.domain.valueobject.OrderApprovalId;

import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private OrderApproval orderApproval;
    private boolean active;
    private final OrderDetail orderDetail;

    //Business Logic
    public void validateOrder(List<String> failureMessages) {
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
        }
        Money totalAmount = orderDetail.getProducts().stream().map(product -> {
            if (!product.isAvailable()) {
                failureMessages.add("Product with id: " + product.getId().getValue());
            }
            //TODO: Will this return the price*quantity for non available products?
            return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO, Money::add);

        if (!totalAmount.equals(orderDetail.getTotalAmount())) {
            failureMessages.add("Price total is not correct for order: " + orderDetail.getId());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(this.getOrderDetail().getId())
                .orderApprovalStatus(orderApprovalStatus)
                .build();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
    }


    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
