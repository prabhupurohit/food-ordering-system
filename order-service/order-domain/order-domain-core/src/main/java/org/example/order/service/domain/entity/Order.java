package org.example.order.service.domain.entity;

import org.example.domain.entity.AggregateRoot;
import org.example.domain.valueobject.*;
import org.example.order.service.domain.exception.OrderDomainException;
import org.example.order.service.domain.valueobject.OrderItemId;
import org.example.order.service.domain.valueobject.StreetAddress;
import org.example.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    //The following fields might not be known at the creation time or may change during runtime
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    //Here all the business logic specific to the Order to be added

    //Following are the validation methods

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();

    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();

    }

    private void validateItemsPrice() {
        Money orderItemsTotal = orderItems.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                + " is not equal to Order items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not valid for product " + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if(orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in the correct state for initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : orderItems) {
            orderItem.initializeOrderItems(super.getId(), new OrderItemId(itemId++));
        }
    }

    //Following are the state changing methods
    //PENDING -> Send to Payment System -> PAID or CANCELLED (If Payment failed)
    //PAID -> Send to Restaurant System -> APPROVED or CANCELLING (If Restaurant rejects request)
    //CANCELLING -> Send to Payment System -> CANCELLED (Compensating transaction)

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in the correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in the correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        orderItems = builder.orderItems;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }


    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    //The newBuilder() method is generated inside the Builder class by the plugin
    //We can take this out and rename to builder() so that we can call this directly on the Order Class
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder orderItems(List<OrderItem> val) {
            orderItems = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
