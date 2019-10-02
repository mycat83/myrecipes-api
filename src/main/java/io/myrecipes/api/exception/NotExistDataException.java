package io.myrecipes.api.exception;

public class NotExistDataException extends RuntimeException {
    public NotExistDataException(String entity, Object id) {
        super(entity + " with ID [" + id.toString() + "]");
    }
}
