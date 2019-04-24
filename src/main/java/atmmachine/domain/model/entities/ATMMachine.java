package atmmachine.domain.model.entities;

import atmmachine.common.ATMMachineConstants;
import atmmachine.domain.model.*;
import atmmachine.domain.model.exception.ATMMachineException;

public class ATMMachine {

    //this should be autowired
    private static ATMMachineRepository repository;

    public AuthenticationResult verifyCredentials(Card card, Pin pin) throws ATMMachineException {
        try {
            Pin actualPin = repository.getPinByCard(card);
            if(!actualPin.doesPinMatch(pin)) {
                return new AuthenticationResult(false, null);
            }
            Account account = repository.getAccountByCard(card);
            Session session = repository.openSessionForAccount(account);
            return new AuthenticationResult(true, session);
        } catch (Exception e) {
            //log exception e
            throw new ATMMachineException(ATMMachineConstants.INTERNAL_SERVER_ERROR);
        }
    }

    public TransactionResult depositMoney(Session session) {
        return null;
    }

    public TransactionResult withdrawMoney(Session session) {
        return null;
    }

    public TransactionResult closeSession(Session session) {
        return null;
    }

}
