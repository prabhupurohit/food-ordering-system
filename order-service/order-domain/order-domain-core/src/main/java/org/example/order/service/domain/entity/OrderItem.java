package org.example.order.service.domain.entity;

import org.example.domain.entity.BaseEntity;
import org.example.domain.valueobject.Money;
import org.example.domain.valueobject.OrderId;
import org.example.order.service.domain.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    //The OrderId is not set as final because this can be dynamically changed to refer to the specific Order at run time.
    //Also, the OrderId may not be known at the time of creation of the OrderItem or set at a different step after the OrderItem is created.
    //However, the other fields are final as we are not going to change those values once created
    private OrderId orderId;

    //OrderItem has a product (one-to-one) relationship
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    //Business logic for Order Item Class goes here
    //The initializeOrderItems() method will be only called from the Order Class which is the root of the aggregate entity
    //Hence the access modifier should be default in this case and not public
    void initializeOrderItems(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero() &&
                price.equals(product.getPrice()) &&
                price.multiply(quantity).equals(subTotal);
    }

    //As the constructor is private, we need to use builder to create this object
    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public Builder orderItemId(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
