package atmmachine.domain.model;

import atmmachine.common.ATMMachineConstants;

public class ATMMachine {

    //this should be autowired
    private static ATMMachineRepository repository;

    public AuthenticationResult verifyCredentials(Card card, Pin pin) throws ATMMachineException {
        try {
            Pin actualPin = repository.getPinByCard(card);
            if(!actualPin.doesPinMatch(pin)) {
                throw new ATMMachineException(ATMMachineConstants.PIN_MISMATCH_EXCEPTION);
            }
            return null;
        } catch(ATMMachineException e) {
            //log exception e
            throw e;
        } catch (Exception e) {
            //log exception e
            throw new ATMMachineException(ATMMachineConstants.INTERNAL_SERVER_ERROR);
        }
    }

    public TransactionResult depositMoney() {
        return null;
    }

    public TransactionResult withdrawMoney() {
        return null;
    }

}
