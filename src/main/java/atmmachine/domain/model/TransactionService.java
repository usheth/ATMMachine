package atmmachine.domain.model;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.TransactionResult;

public interface TransactionService {

  TransactionResult addMoneyToAccount(Account account, Money money);

  TransactionResult withdrawAmountFromAccount(Account account, Amount amount);

  Amount getAccountBalance(Account account);
}
