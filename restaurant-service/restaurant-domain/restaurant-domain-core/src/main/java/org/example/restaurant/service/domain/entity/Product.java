package org.example.restaurant.service.domain.entity;

import org.example.domain.entity.BaseEntity;
import org.example.domain.valueobject.Money;
import org.example.domain.valueobject.ProductId;

//Order Service has also a Product which has limited information which are required for carrying out
//business logic of Order. As per DDD, other bounded context can also duplicate an entity with
//specific information. Following Product has more information and relevant in the Restaurant bounded context.

public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;
    //No intention of changing the product quantity after setting up the product
    private final int quantity;
    private boolean available;

    //Business Logic for Restaurant Products
    public void updateWithConfirmedNamePriceAndAvailability(String name, Money Price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    private Product(Builder builder) {
        super.setId(builder.productId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        available = builder.available;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public static Builder builder() {
        return new Builder();
    }



    public static final class Builder {
        private ProductId productId;
        private String name;
        private Money price;
        private int quantity;
        private boolean available;

        private Builder() {
        }

        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
