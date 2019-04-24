package atmmachine.domain.model.entities;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.transaction.Transaction;

import java.util.List;

public class Session {

    private String type = "Session";
    //assumes these fields are initialized by the repository when a new session is created
    private String sessionId;
    private Customer customer;
    private Account account;
    private long sessionDuration;
    private List<Transaction> transactionsInThisSession;

    public Account getAccount() {
        return account;
    }

    public void addTransactionToSession(Transaction transaction) {
        transactionsInThisSession.add(transaction);
    }
}
