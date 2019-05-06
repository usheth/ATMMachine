package atmmachine.domain.model;

public class AuthenticationResult {

    private String type = "AuthenticationResult";
    private boolean authenticationStatus;
    private String token;

    public AuthenticationResult(boolean authenticationStatus, String token) {
        this.authenticationStatus = authenticationStatus;
        this.token = token;
    }

    public void setAuthenticationSucceeded() {
        authenticationStatus = true;
    }

    public void setAuthenticationFailed() {
        authenticationStatus = false;
    }

    public boolean authenticted() { return authenticationStatus; }

    public String getToken() {
        return token;
    }
}
