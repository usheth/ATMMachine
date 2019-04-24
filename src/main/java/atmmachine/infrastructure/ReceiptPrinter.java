package atmmachine.infrastructure;

import atmmachine.domain.model.entities.Session;

public class ReceiptPrinter {

    private static ReceiptPrinter instance;

    public static synchronized ReceiptPrinter getInstance() {
        if(instance == null) {
            instance = new ReceiptPrinter();
        }
        return instance;
    }

    public void printReceipt(Session session) {

    }

}
