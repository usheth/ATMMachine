package atmmachine.domain.model;

import atmmachine.domain.model.entities.Session;

public class AuthenticationResult {

    private String type = "AuthenticationResult";
    private boolean authenticationStatus;
    private Session session;

    public AuthenticationResult(boolean authenticationStatus, Session session) {
        this.authenticationStatus = authenticationStatus;
        this.session = session;
    }

    public void setAuthenticationSucceeded() {
        authenticationStatus = true;
    }

    public void setAuthenticationFailed() {
        authenticationStatus = false;
    }

}
