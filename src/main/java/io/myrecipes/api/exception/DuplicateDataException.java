package io.myrecipes.api.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(Class<?> clazz, Object id) {
        super(clazz.getName() + " with ID [" + id.toString() + "]");
    }
}
