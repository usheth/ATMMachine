package atmmachine.domain.services;

import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.transaction.DepositAmountTransaction;
import atmmachine.domain.model.transaction.Transaction;
import atmmachine.domain.model.transaction.WithdrawAmountTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRecorder {

    private Map<String, List<Transaction>> transactions;

    public TransactionRecorder() {
        transactions = new HashMap<>();
    }

    public void initializeSession(String token) {
        transactions.put(token, new ArrayList<>());
    }

    public void addNewDepositMoneyTransaction(String token, Money money) {
        transactions.get(token).add(new DepositAmountTransaction(money));
    }

    public void addNewWithdrawAmountTransaction(String token, Amount amount) {
        transactions.get(token).add(new WithdrawAmountTransaction(amount));
    }

    public List<Transaction> getAllTransactionsByToken(String token) {
        return transactions.get(token);
    }

    public void removeTransactionsForToken(String token) {
        transactions.remove(token);
    }

}
