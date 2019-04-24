package atmmachine.domain.model;

public class TransactionResult {

    private String type = "TransactionResult";
    boolean transactionStatus;

    public void setTransactionSucceeded() {
        transactionStatus = true;
    }

    public void setTransactionFailed() {
        transactionStatus = false;
    }

}
