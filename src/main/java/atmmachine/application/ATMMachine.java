package atmmachine.application;

import atmmachine.common.ATMMachineConstants;
import atmmachine.common.TypeConverter;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.TransactionRecorder;
import atmmachine.domain.model.TransactionService;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.exception.AuthenticationServiceException;
import atmmachine.domain.model.exception.TransactionServiceException;
import atmmachine.domain.model.transaction.Transaction;
import atmmachine.domain.model.transaction.TransactionResult;
import atmmachine.infrastructure.CashDispenser;
import atmmachine.infrastructure.EmailSender;
import atmmachine.infrastructure.ReceiptPrinter;
import java.util.List;

public class ATMMachine {

  //ideally these are autowired
  private AuthenticationService authenticationService;
  private TransactionService transactionService;
  private TransactionRecorder transactionRecorder;
  private CashDispenser cashDispenser;

  public ATMMachine(AuthenticationService authenticationService,
      TransactionService transactionService, TransactionRecorder transactionRecorder,
      CashDispenser cashDispenser) {
    this.authenticationService = authenticationService;
    this.transactionRecorder = transactionRecorder;
    this.transactionService = transactionService;
    this.cashDispenser = cashDispenser;
  }

  public AuthenticationResult verifyCredentials(Card card, Pin pin) {
    try {
      return authenticationService.authenticate(card, pin);
    } catch (Exception e) {
      //log exception e
      return new AuthenticationResult(false, e.getMessage());
    }
  }

  private boolean isTokenValid(long accountId, String token)
      throws AuthenticationServiceException {
    return authenticationService.isTokenValid(accountId, token);
  }

  public TransactionResult depositMoney(String token, long accountId, Money money) {
    try {
      if (!isTokenValid(accountId, token)) {
        return new TransactionResult(false, ATMMachineConstants.INVALID_TOKEN);
      }
      TransactionResult result = transactionService.addMoneyToAccount(accountId, money);
      if (result.didTransactionSucceed()) {
        transactionRecorder.addNewDepositMoneyTransaction(token, money);
      }
      return result;
    } catch (Exception e) {
      //log exception e
      return new TransactionResult(false, e.getMessage());
    }
  }

  private boolean isBalanceSufficientForWithdrawal(long accountId, Amount withdrawalAmount)
      throws TransactionServiceException {
    Amount currentBalance = new Amount(
        TypeConverter.stringToDouble(transactionService.getAccountBalance(accountId).getMessage()));
    return currentBalance.getValue() >= withdrawalAmount.getValue();
  }

  private boolean doesATMHaveEnoughCash(Amount withdrawalAmount) {
    return cashDispenser.getTotalCashInATM().getValue() >= withdrawalAmount.getValue();
  }

  public TransactionResult withdrawMoney(String token, long accountId, Amount amount) {
    //maybe add validation for the amount (multiple of 10 etc)
    try {
      if (!isTokenValid(accountId, token)) {
        //maybe the invalid session case should be handled separately, possible by throwing an exception
        return new TransactionResult(false, ATMMachineConstants.INVALID_TOKEN);
      } else if (!doesATMHaveEnoughCash(amount)) {
        return new TransactionResult(false, ATMMachineConstants.INSUFFICIENT_CASH_IN_ATM);
      } else if (!isBalanceSufficientForWithdrawal(accountId, amount)) {
        return new TransactionResult(false, ATMMachineConstants.INSUFFICIENT_BALANCE_IN_ACCOUNT);
      }
      TransactionResult result = transactionService.withdrawAmountFromAccount(accountId, amount);
      if (result.didTransactionSucceed()) {
        transactionRecorder.addNewWithdrawAmountTransaction(token, amount);
      }
      return result;
    } catch (Exception e) {
      //log exception e
      return new TransactionResult(false, e.getMessage());
    }
  }

  public TransactionResult getAccountBalance(String token, long accountId) {
    try {
      if (!isTokenValid(accountId, token)) {
        return new TransactionResult(false, ATMMachineConstants.INVALID_TOKEN);
      }
      return transactionService.getAccountBalance(accountId);
    } catch (Exception e) {
      //log exception e
      return new TransactionResult(false, e.getMessage());
    }
  }

  public AuthenticationResult logout(String token, long accountId) {
    try {
      if (!isTokenValid(accountId, token)) {
        return new AuthenticationResult(false, ATMMachineConstants.INVALID_TOKEN);
      }
      AuthenticationResult result = authenticationService.invalidateToken(token);
      if (result.authenticted()) {
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
      return new AuthenticationResult(false, e.getMessage());
    }
  }

}
