package atmmachine.domain.model.transaction;

public class TransactionResult {

    private String type = "TransactionResult";
    boolean transactionStatus;

    public TransactionResult(boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public boolean didTransactionSucceed() {
        return transactionStatus;
    }
}
