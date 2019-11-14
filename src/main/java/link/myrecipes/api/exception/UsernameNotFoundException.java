package link.myrecipes.api.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super("User [" + username + "] does not exist.");
    }
}
