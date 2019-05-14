package atmmachine.domain.model.exception;

public class AuthenticationServiceException extends Exception {
    public AuthenticationServiceException(String message) {
        super(message);
    }
}
