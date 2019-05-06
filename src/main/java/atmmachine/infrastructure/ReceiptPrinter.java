package atmmachine.infrastructure;

import atmmachine.domain.model.transaction.Transaction;

import java.util.List;

public class ReceiptPrinter {

    private static ReceiptPrinter instance;

    public static synchronized ReceiptPrinter getInstance() {
        if(instance == null) {
            instance = new ReceiptPrinter();
        }
        return instance;
    }

    public void printReceipt(List<Transaction> transactionsInSession) {

    }

}
