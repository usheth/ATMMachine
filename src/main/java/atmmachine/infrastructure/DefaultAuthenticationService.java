package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class DefaultAuthenticationService implements AuthenticationService {

    private ATMMachineRepository repository;

    public DefaultAuthenticationService(ATMMachineRepository repository) {
        this.repository = repository;
    }

    @Bean
    private ATMMachineRepository getRepository() {
        return new SecureRemoteServerRepository();
    }

    @Override
    public AuthenticationResult authenticate(Card card, Pin pin) {
        boolean authenticated = repository.authenticateCardFromPin(card, pin);
        if(!authenticated) {
            repository.handleFailedAuthenticationForCard(card);
            return new AuthenticationResult(false,"Authentication failed");
        }
        String token = repository.createAccessTokenForCard(card);
        return new AuthenticationResult(true, token);
    }

    @Override
    public boolean isTokenValid(long accountId, String token) {
        return repository.isTokenValid(accountId, token);
    }

    @Override
    public AuthenticationResult invalidateToken(String token) {
        return repository.logout(token);
    }

}
