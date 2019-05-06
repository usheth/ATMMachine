package atmmachine.infrastructure;

import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.DepositAmountTransaction;
import atmmachine.domain.model.transaction.Transaction;
import atmmachine.domain.model.transaction.WithdrawAmountTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultTransactionRecorder implements atmmachine.domain.model.TransactionRecorder {

    private Map<String, List<Transaction>> transactions;

    public DefaultTransactionRecorder() {
        transactions = new HashMap<>();
    }

    @Override
    public void initializeSession(String token) {
        transactions.put(token, new ArrayList<>());
    }

    @Override
    public void addNewDepositMoneyTransaction(String token, Money money) {
        transactions.get(token).add(new DepositAmountTransaction(money));
    }

    @Override
    public void addNewWithdrawAmountTransaction(String token, Amount amount) {
        transactions.get(token).add(new WithdrawAmountTransaction(amount));
    }

    @Override
    public List<Transaction> getAllTransactionsByToken(String token) {
        return transactions.get(token);
    }

    @Override
    public void removeTransactionsForToken(String token) {
        transactions.remove(token);
    }

}
