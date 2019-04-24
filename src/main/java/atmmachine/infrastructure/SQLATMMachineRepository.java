package atmmachine.infrastructure;

import atmmachine.domain.model.*;
import atmmachine.domain.model.entities.*;
import atmmachine.domain.model.transaction.TransactionResult;

public class SQLATMMachineRepository implements ATMMachineRepository {

    @Override
    public Pin getPinByCard(Card card) {
        return null;
    }

    @Override
    public Account getAccountByCard(Card card) {
        return null;
    }

    @Override
    public Session createNewSessionForCard(Card card) {
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
    public TransactionResult subtractMoneyFromAccount(Account account, Money money) {
        return null;
    }

    @Override
    public TransactionResult subtractAmountFromAccount(Account account, Amount amount) {
        return null;
    }

    @Override
    public boolean isSessionValid(Session session) {
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
    public TransactionResult closeSession(Session session) {
        return null;
    }

}
