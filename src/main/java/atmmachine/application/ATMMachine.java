package atmmachine.application;

import atmmachine.domain.model.*;
import atmmachine.domain.model.entities.*;
import atmmachine.domain.model.transaction.Transaction;
import atmmachine.domain.model.transaction.TransactionResult;
import atmmachine.infrastructure.CashDispenser;
import atmmachine.infrastructure.EmailSender;
import atmmachine.infrastructure.ReceiptPrinter;

import java.util.List;

public class ATMMachine {

    //ideally these are autowired
    private atmmachine.domain.model.AuthenticationService authenticationService;
    private TransactionService transactionService;
    private atmmachine.domain.model.TransactionRecorder transactionRecorder;

    public ATMMachine(AuthenticationService authenticationService,
        atmmachine.domain.model.TransactionService transactionService, TransactionRecorder transactionRecorder) {
        this.authenticationService = authenticationService;
        this.transactionRecorder = transactionRecorder;
        this.transactionService = transactionService;
    }

    public AuthenticationResult verifyCredentials(Card card, Pin pin) {
        try {
            return authenticationService.authenticate(card, pin);
        } catch (Exception e) {
            //log exception e
            return new AuthenticationResult(false, e.getMessage());
        }
    }

    private boolean isTokenValid(Account account, String token) {
        return authenticationService.isTokenValid(account, token);
    }

    public TransactionResult depositMoney(String token, Account account, Money money) {
        if(!isTokenValid(account, token)) {
            return new TransactionResult(false);
        }
        try {
            TransactionResult result = transactionService.addMoneyToAccount(account, money);
            if(result.didTransactionSucceed()) {
                transactionRecorder.addNewDepositMoneyTransaction(token, money);
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        }
    }

    private boolean isBalanceSufficientForWithdrawal(Account account, Amount withdrawalAmount) {
        Amount currentBalance = transactionService.getAccountBalance(account);
        return currentBalance.getAmount() >= withdrawalAmount.getAmount();
    }

    private boolean doesATMHaveEnoughCash(Amount withdrawalAmount) {
        CashDispenser cashDispenser = CashDispenser.getInstance();
        return cashDispenser.getTotalCashInATM().getAmount() >= withdrawalAmount.getAmount();
    }

    public TransactionResult withdrawMoney(String token, Account account, Amount amount) {
        //maybe add validation for the amount (multiple of 10 etc)
        if(!isTokenValid(account, token) || !doesATMHaveEnoughCash(amount) || !isBalanceSufficientForWithdrawal(account, amount)) {
            //maybe the invalid session case should be handled separately, possible by throwing an exception
            return new TransactionResult(false);
        }
        try {
            TransactionResult result = transactionService.withdrawAmountFromAccount(account, amount);
            if(result.didTransactionSucceed()) {
                transactionRecorder.addNewWithdrawAmountTransaction(token, amount);
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new TransactionResult(false);
        }
    }

    public Amount getAccountBalance(String token, Account account) {
        if(!isTokenValid(account, token)) {
            return null;
        }
        try {
            return transactionService.getAccountBalance(account);
        } catch (Exception e) {
            //log exception e
            return null;
        }
    }

    public AuthenticationResult logout(String token, Account account) {
        if(!isTokenValid(account, token)) {
            return new AuthenticationResult(false, null);
        }
        try {
            AuthenticationResult result = authenticationService.invalidateToken(token);
            if(result.authenticted()) {
                List<Transaction> transactions = transactionRecorder.getAllTransactionsByToken(token);
                EmailSender emailSender = EmailSender.getInstance();
                emailSender.sendEmailForSession(transactions);
                ReceiptPrinter receiptPrinter = ReceiptPrinter.getInstance();
                receiptPrinter.printReceipt(transactions);
                transactionRecorder.removeTransactionsForToken(token);
            }
            return result;
        } catch (Exception e) {
            //log exception e
            return new AuthenticationResult(false, null);
        }
    }

}