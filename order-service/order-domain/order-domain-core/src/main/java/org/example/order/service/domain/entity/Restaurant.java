package org.example.order.service.domain.entity;

import org.example.domain.entity.AggregateRoot;
import org.example.domain.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    //This is required to get the builder for the Restaurant
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
