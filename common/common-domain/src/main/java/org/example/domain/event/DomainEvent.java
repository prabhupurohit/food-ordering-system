package org.example.domain.event;

//T represents the entity from which the event is generated
public interface DomainEvent<T> {

    void fire();

}
