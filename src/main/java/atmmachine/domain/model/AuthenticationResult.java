package atmmachine.domain.model;

public class AuthenticationResult {

    private String type = "AuthenticationResult";
    private boolean authenticationStatus;
    private String token;
    private String errorMessage;

    public AuthenticationResult(boolean authenticationStatus, String param) {
        this.authenticationStatus = authenticationStatus;
        if(authenticationStatus) {
            this.token = param;
        } else {
            this.errorMessage = param;
        }
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

    public String getErrorMessage() {
        return errorMessage;
    }
}
