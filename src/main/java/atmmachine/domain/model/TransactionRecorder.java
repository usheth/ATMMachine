package atmmachine.domain.model;

import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.Transaction;
import java.util.List;

public interface TransactionRecorder {

  void initializeSession(String token);

  void addNewDepositMoneyTransaction(String token, Money money);

  void addNewWithdrawAmountTransaction(String token, Amount amount);

  List<Transaction> getAllTransactionsByToken(String token);

  void removeTransactionsForToken(String token);
}
