package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.entities.*;
import atmmachine.domain.model.transaction.TransactionResult;


// Make REST API calls here.
public class SecureRemoteServerRepository implements ATMMachineRepository {
    @Override
    public Pin getPinByCard(Card card) {
        return null;
    }

    @Override
    public Account getAccountByCard(Card card) {
        return null;
    }

    @Override
    public Customer getCustomerByCard(Card card) {
        return null;
    }

    @Override
    public void getWriteLockOnAccount(Account account) {

    }

    @Override
    public void releaseWriteLockOnAccount(Account account) {

    }

    @Override
    public TransactionResult addMoneyToAccount(Account account, Money money) {
        return null;
    }

    @Override
    public TransactionResult subtractAmountFromAccount(Account account, Amount amount) {
        return null;
    }

    @Override
    public boolean isTokenValid(Account account, String session) {
        return false;
    }

    @Override
    public Amount getAccountBalance(Account account) {
        return null;
    }

    @Override
    public void getReadLockOnAccount(Account account) {

    }

    @Override
    public void releaseReadLockOnAccount(Account account) {

    }

    @Override
    public boolean authenticateCardFromPin(Card card, Pin pin) {
        return false;
    }

    @Override
    public void handleFailedAuthenticationForCard(Card card) {

    }

    @Override
    public AuthenticationResult logout(String token) {
        return null;
    }

    @Override
    public String createAccessTokenForCard(Card card) {
        return null;
    }

}
