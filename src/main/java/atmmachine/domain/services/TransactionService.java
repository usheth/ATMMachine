package atmmachine.domain.services;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.TransactionResult;

public class TransactionService {

    private ATMMachineRepository repository;

    public TransactionResult addMoneyToAccount(Account account, Money money) {
        try {
            repository.getWriteLockOnAccount(account);
            TransactionResult result = repository.addMoneyToAccount(account, money);
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        } finally {
            repository.releaseWriteLockOnAccount(account);
        }
    }

    public TransactionResult withdrawAmountFromAccount(Account account, Amount amount) {
        try {
            repository.getWriteLockOnAccount(account);
            TransactionResult result = repository.subtractAmountFromAccount(account, amount);
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        } finally {
            repository.releaseWriteLockOnAccount(account);
        }
    }

    public Amount getAccountBalance(Account account) {
        try {
            repository.getReadLockOnAccount(account);
            return repository.getAccountBalance(account);
        } catch (Exception e) {
            //log exception e
            return null;
        } finally {
            repository.releaseReadLockOnAccount(account);
        }
    }

}
