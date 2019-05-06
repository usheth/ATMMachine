package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;

public class DefaultAuthenticationService implements AuthenticationService {

    //Ideally this is autowired
    private ATMMachineRepository repository;

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
    public boolean isTokenValid(Account account, String token) {
        return repository.isTokenValid(account, token);
    }

    @Override
    public AuthenticationResult invalidateToken(String token) {
        return repository.logout(token);
    }

}
