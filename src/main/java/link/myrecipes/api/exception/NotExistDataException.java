package link.myrecipes.api.exception;

public class NotExistDataException extends RuntimeException {
    public NotExistDataException(Class<?> clazz, Object id) {
        super(clazz.getName() + " with ID [" + id.toString() + "]");
    }
}
