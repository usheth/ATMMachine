package atmmachine.domain.model;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.exception.TransactionServiceException;
import atmmachine.domain.model.transaction.TransactionResult;

public interface TransactionService {

  TransactionResult addMoneyToAccount(Account account, Money money) throws TransactionServiceException;

  TransactionResult withdrawAmountFromAccount(Account account, Amount amount) throws TransactionServiceException;

  Amount getAccountBalance(Account account) throws TransactionServiceException;
}
