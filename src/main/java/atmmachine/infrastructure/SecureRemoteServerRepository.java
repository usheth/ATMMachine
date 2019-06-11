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
    public void getWriteLockOnAccount(long accountId) {

    }

    @Override
    public void releaseWriteLockOnAccount(long accountId) {

    }

    @Override
    public void getReadLockOnAccount(long accountId) {

    }

    @Override
    public void releaseReadLockOnAccount(long accountId) {

    }

    @Override
    public TransactionResult addMoneyToAccount(long accountId, Money money) {
        return null;
    }

    @Override
    public TransactionResult subtractAmountFromAccount(long accountId, Amount amount) {
        return null;
    }

    @Override
    public boolean isTokenValid(long accountId, String token) {
        return Math.random() >= 0.5;
    }

    @Override
    public Amount getAccountBalance(long accountId) {
        return null;
    }

    @Override
    public boolean authenticateCardFromPin(Card card, Pin pin) {
        return Math.random() >= 0.5;
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
        return "DummyToken";
    }

}
