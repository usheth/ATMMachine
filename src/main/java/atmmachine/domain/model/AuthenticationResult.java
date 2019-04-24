package atmmachine.domain.model;

public class AuthenticationResult {

    private String type = "AuthenticationResult";
    private boolean authenticationStatus;

    public void setAuthenticationSucceeded() {
        authenticationStatus = true;
    }

    public void setAuthenticationFailed() {
        authenticationStatus = false;
    }

}
