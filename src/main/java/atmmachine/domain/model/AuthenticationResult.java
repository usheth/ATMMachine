package atmmachine.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthenticationResult {

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

    public boolean authenticted() { return authenticationStatus; }

    public String getToken() {
        return token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
