package com.example.shared.exception;

public class MissingEntityException extends Exception {

    public final Class<?> entityClass;

    public final Long entityID;

    public MissingEntityException(Class<?> entityClass, Long entityID) {
        super(String.format("Missing entity of type %s with id %d", entityClass.getName(), entityID));

        this.entityClass = entityClass;
        this.entityID = entityID;
    }
}
