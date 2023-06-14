package org.example.domain.valueobject;

import java.util.Objects;

public abstract class BaseId<T> {

    //This is final so that it can't be modified
    private final T value;

    //The constructor is protected so that only the extending classes can construct and set the value
    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return Objects.equals(value, baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
