package atmmachine.domain.model.entities;

import atmmachine.domain.model.*;
import atmmachine.domain.model.transaction.DepositAmountTransaction;
import atmmachine.domain.model.transaction.TransactionResult;
import atmmachine.domain.model.transaction.WithdrawAmountTransaction;
import atmmachine.infrastructure.CashDispenser;
import atmmachine.infrastructure.EmailSender;
import atmmachine.infrastructure.ReceiptPrinter;

public class ATMMachine {

    //ideally this is autowired
    private static ATMMachineRepository repository;

    public AuthenticationResult verifyCredentials(Card card, Pin pin) {
        try {
            Pin actualPin = repository.getPinByCard(card);
            if(!actualPin.doesPinMatch(pin)) {
                return new AuthenticationResult(false, null);
            }
            Session session = repository.createNewSessionForCard(card);
            return new AuthenticationResult(true, session);
        } catch (Exception e) {
            //log exception e
            return new AuthenticationResult(false, null);
        }
    }

    private boolean isSessionValid(Session session) {
        return repository.isSessionValid(session);
    }

    public TransactionResult depositMoney(Session session, Money money) {
        if(!isSessionValid(session)) {
            return new TransactionResult(false);
        }
        try {
            repository.getWriteLockOnAccount(session.getAccount());
            TransactionResult result = repository.addMoneyToAccount(session.getAccount(), money);
            if(result.didTransactionSucceed()) {
                session.addTransactionToSession(new DepositAmountTransaction(money));
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        } finally {
            repository.releaseWriteLockOnAccount(session.getAccount());
        }
    }

    private boolean isBalanceSufficientForWithdrawal(Session session, Amount withdrawalAmount) {
        Amount currentBalance = getAccountBalance(session);
        return currentBalance.getAmount() >= withdrawalAmount.getAmount();
    }

    private boolean doesATMHaveEnoughCash(Amount withdrawalAmount) {
        CashDispenser cashDispenser = CashDispenser.getInstance();
        return cashDispenser.getTotalCashInATM().getAmount() >= withdrawalAmount.getAmount();
    }

    public TransactionResult withdrawMoney(Session session, Amount amount) {
        //maybe add validation for the amount (multiple of 10 etc)
        if(!isSessionValid(session) || !doesATMHaveEnoughCash(amount) || !isBalanceSufficientForWithdrawal(session, amount)) {
            //maybe the invalid session case should be handled separately, possible by throwing an exception
            return new TransactionResult(false);
        }
        try {
            repository.getWriteLockOnAccount(session.getAccount());
            TransactionResult result = repository.subtractAmountFromAccount(session.getAccount(), amount);
            if(result.didTransactionSucceed()) {
                CashDispenser cashDispenser = CashDispenser.getInstance();
                Money dispensedMoney = cashDispenser.dispenseAmountAndGetDenominations(amount);
                session.addTransactionToSession(new WithdrawAmountTransaction(dispensedMoney));
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        } finally {
            repository.releaseWriteLockOnAccount(session.getAccount());
        }
    }

    public Amount getAccountBalance(Session session) {
        if(!isSessionValid(session)) {
            return null;
        }
        try {
            repository.getReadLockOnAccount(session.getAccount());
            return repository.getAccountBalance(session.getAccount());
        } catch (Exception e) {
            //log exception e
            return null;
        } finally {
            repository.releaseReadLockOnAccount(session.getAccount());
        }
    }

    public TransactionResult closeSession(Session session) {
        if(!isSessionValid(session)) {
            return new TransactionResult(false);
        }
        try {
            TransactionResult result = repository.closeSession(session);
            if(result.didTransactionSucceed()) {
                EmailSender emailSender = EmailSender.getInstance();
                emailSender.sendEmailForSession(session);
                ReceiptPrinter receiptPrinter = ReceiptPrinter.getInstance();
                receiptPrinter.printReceipt(session);
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        }
    }

}
