package atmmachine.infrastructure;

import atmmachine.domain.model.transaction.Transaction;

import java.util.List;

public class EmailSender {

    private static EmailSender instance;

    public static synchronized EmailSender getInstance() {
        if(instance == null) {
            instance = new EmailSender();
        }
        return instance;
    }

    public void sendEmailForSession(List<Transaction> transactionsInSession) {

    }

}
