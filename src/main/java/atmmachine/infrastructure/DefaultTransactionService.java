package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.TransactionResult;

public class DefaultTransactionService implements atmmachine.domain.model.TransactionService {

    private ATMMachineRepository repository;

    @Override
    public TransactionResult addMoneyToAccount(long accountId, Money money) {
        try {
            repository.getWriteLockOnAccount(accountId);
            TransactionResult result = repository.addMoneyToAccount(accountId, money);
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false, e.getMessage());
        } finally {
            repository.releaseWriteLockOnAccount(accountId);
        }
    }

    @Override
    public TransactionResult withdrawAmountFromAccount(long accountId, Amount amount) {
        try {
            repository.getWriteLockOnAccount(accountId);
            TransactionResult result = repository.subtractAmountFromAccount(accountId, amount);
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false, e.getMessage());
        } finally {
            repository.releaseWriteLockOnAccount(accountId);
        }
    }

    @Override
    public Amount getAccountBalance(long accountId) {
        try {
            repository.getReadLockOnAccount(accountId);
            return repository.getAccountBalance(accountId);
        } catch (Exception e) {
            //log exception e
            return null;
        } finally {
            repository.releaseReadLockOnAccount(accountId);
        }
    }

}
