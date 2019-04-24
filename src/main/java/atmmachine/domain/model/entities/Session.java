package atmmachine.domain.model.entities;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.transaction.Transaction;

import java.util.List;

public class Session {

    private String type = "Session";
    private Account account;
    private long sessionDuration;
    List<Transaction> transactionsInThisSession;

}
