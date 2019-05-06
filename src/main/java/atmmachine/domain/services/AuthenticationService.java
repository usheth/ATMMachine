package atmmachine.domain.services;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;

public class AuthenticationService {

    //Ideally this is autowired
    private ATMMachineRepository repository;

    public AuthenticationResult authenticate(Card card, Pin pin) {
        boolean authenticated = repository.authenticateCardFromPin(card, pin);
        if(!authenticated) {
            repository.handleFailedAuthenticationForCard(card);
            return new AuthenticationResult(false, null);
        }
        String token = repository.createAccessTokenForCard(card);
        return new AuthenticationResult(true, token);
    }

    public boolean isTokenValid(Account account, String token) {
        return repository.isTokenValid(account, token);
    }

    public AuthenticationResult logout(String token) {
        return repository.logout(token);
    }

}
