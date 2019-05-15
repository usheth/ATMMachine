package atmmachine.domain.model.transaction;

public class TransactionResult {

    private String type = "TransactionResult";
    private boolean transactionStatus;
    private String message;

    public TransactionResult(boolean transactionStatus, String message) {
        this.transactionStatus = transactionStatus;
        this.message = message;
    }

    public boolean didTransactionSucceed() {
        return transactionStatus;
    }

    public String getMessage() {
        return message;
    }
}
