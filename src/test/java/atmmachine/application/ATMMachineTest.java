package atmmachine.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import atmmachine.common.ATMMachineConstants;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.TransactionRecorder;
import atmmachine.domain.model.TransactionService;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Cash;
import atmmachine.domain.model.entities.Customer;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.entities.SavingsAccount;
import atmmachine.domain.model.exception.AuthenticationServiceException;
import atmmachine.domain.model.exception.TransactionServiceException;
import atmmachine.domain.model.transaction.Transaction;
import atmmachine.domain.model.transaction.TransactionResult;
import atmmachine.infrastructure.CashDispenser;
import java.util.ArrayList;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class ATMMachineTest {

  private ATMMachine atmMachine;
  private long testAccount;
  private Customer testCustomer;
  @Mock private AuthenticationService authenticationService;
  @Mock private TransactionService transactionService;
  @Mock private TransactionRecorder transactionRecorder;
  @Mock private CashDispenser cashDispenser;
  private static final String INVALID_CREDENTIALS = "Invalid Credentials";
  private static final String AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE = "Could not connect to database";
  private static final String TRANSACTION_SERVICE_EXCEPTION_MESSAGE = "";
  private static final String DEPOSIT_SUCCEEDED = "";
  private static final String DEPOSIT_FAILED = "";
  private static final double HUNDRED_DOLLARS = 100;
  private static final double THOUSAND_DOLLARS = 1000;
  private static final String WITHDRAW_SUCCEEDED = "";
  private static final String WITHDRAW_FAILED = "";
  private static final String CARD_NO = "";

  @BeforeMethod
  public void before() {
    MockitoAnnotations.initMocks(this);
    atmMachine = new ATMMachine(authenticationService, transactionService,
        transactionRecorder, cashDispenser);
    testAccount = 1L;
    testCustomer = new Customer();
  }

  @Test
  public void testVerificationOfCredentialsShouldPass() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.authenticate(any(Card.class), any(Pin.class)))
        .thenReturn(new AuthenticationResult(true, token));
    Card card = new Card(CARD_NO);
    Pin pin = new Pin();

    // when
    AuthenticationResult result = atmMachine.verifyCredentials(card, pin);

    // then
    //verify(atmMachine).verifyCredentials(card, pin);
    assertThat(result).isNotNull();
    assertThat(result.authenticted()).isTrue();
    assertThat(result.getToken()).isNotNull();
    assertThat(result.getToken()).isEqualTo(token);
    assertThat(result.getErrorMessage()).isNull();
  }

  @Test
  public void testVerificationOfCredentialsShouldFail() throws Exception {
    // given
    when(authenticationService.authenticate(any(Card.class), any(Pin.class)))
        .thenReturn(new AuthenticationResult(false, INVALID_CREDENTIALS));
    Card card = new Card(CARD_NO);
    Pin pin = new Pin();

    // when
    AuthenticationResult result = atmMachine.verifyCredentials(card, pin);

    // then
    //verify(atmMachine).verifyCredentials(card, pin);
    assertThat(result).isNotNull();
    assertThat(result.authenticted()).isFalse();
    assertThat(result.getToken()).isNull();
    assertThat(result.getErrorMessage()).isNotNull().isEqualTo(INVALID_CREDENTIALS);
  }

  @Test
  public void testVerificationOfCredentialsShouldThrowException() throws Exception {
    // given
    when(authenticationService.authenticate(any(Card.class), any(Pin.class)))
        .thenThrow(new AuthenticationServiceException(AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE));
    Card card = new Card(CARD_NO);
    Pin pin = new Pin();

    // when
    AuthenticationResult result = atmMachine.verifyCredentials(card, pin);

    // then
    //verify(atmMachine).verifyCredentials(card, pin);
    assertThat(result).isNotNull();
    assertThat(result.authenticted()).isFalse();
    assertThat(result.getToken()).isNull();
    assertThat(result.getErrorMessage()).isNotNull().isEqualTo(AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE);
  }

  // New cases

  @Test
  public void testDepositMoneyShouldSucceed() throws Exception {
    // given
    Money someCash = new Cash();
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token)).thenReturn(true);
    when(transactionService.addMoneyToAccount(testAccount, someCash))
        .thenReturn(new TransactionResult(true, DEPOSIT_SUCCEEDED));

    // when
    TransactionResult depositTransactionResult = atmMachine.depositMoney(token, testAccount, someCash);

    // then
    assertThat(depositTransactionResult).isNotNull();
    assertThat(depositTransactionResult.didTransactionSucceed()).isTrue();
    assertThat(depositTransactionResult.getMessage()).isNotNull();
    assertThat(depositTransactionResult.getMessage()).isEqualTo(DEPOSIT_SUCCEEDED);
  }

  @Test
  public void testDepositMoneyWithInvalidTokenShouldFail() throws Exception {
    // given
    String invalidToken = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, invalidToken)).thenReturn(false);
    Money someCash = new Cash();

    // when
    TransactionResult depositTransactionResult = atmMachine.depositMoney(invalidToken, testAccount, someCash);

    // then
    assertThat(depositTransactionResult).isNotNull();
    assertThat(depositTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(depositTransactionResult.getMessage()).isNotNull();
    assertThat(depositTransactionResult.getMessage()).isEqualTo(ATMMachineConstants.INVALID_TOKEN);
  }

  @Test
  public void testDepositMoneyShouldFail() throws Exception {
    // given
    Money someCash = new Cash();
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token)).thenReturn(true);
    when(transactionService.addMoneyToAccount(testAccount, someCash))
        .thenReturn(new TransactionResult(false, DEPOSIT_FAILED));

    // when
    TransactionResult depositTransactionResult = atmMachine.depositMoney(token, testAccount, someCash);

    // then
    assertThat(depositTransactionResult).isNotNull();
    assertThat(depositTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(depositTransactionResult.getMessage()).isNotNull();
    assertThat(depositTransactionResult.getMessage()).isEqualTo(DEPOSIT_FAILED);
  }

  @Test
  public void testDepositMoneyShouldThrowException() throws Exception {
    // given
    Money someCash = new Cash();
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(transactionService.addMoneyToAccount(testAccount, someCash))
        .thenThrow(new TransactionServiceException(TRANSACTION_SERVICE_EXCEPTION_MESSAGE));

    // when
    TransactionResult depositTransactionResult = atmMachine.depositMoney(token, testAccount, someCash);

    // then
    //verify(atmMachine).verifyCredentials(card, pin);
    assertThat(depositTransactionResult).isNotNull();
    assertThat(depositTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(depositTransactionResult.getMessage()).isNotNull();
    assertThat(depositTransactionResult.getMessage()).isEqualTo(TRANSACTION_SERVICE_EXCEPTION_MESSAGE);
  }

  @Test
  public void testWithdrawAmountShouldSucceed() throws Exception {
    // given
    Amount hundredDollars = new Amount(HUNDRED_DOLLARS);
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(cashDispenser.getTotalCashInATM())
        .thenReturn(new Amount(THOUSAND_DOLLARS));
    when(transactionService.getAccountBalance(testAccount))
        .thenReturn(new TransactionResult(true, Double.toString(THOUSAND_DOLLARS)));
    when(transactionService.withdrawAmountFromAccount(testAccount, hundredDollars))
        .thenReturn(new TransactionResult(true, WITHDRAW_SUCCEEDED));

    // when
    TransactionResult withdrawTransactionResult = atmMachine.withdrawMoney(token, testAccount, hundredDollars);

    // then
    assertThat(withdrawTransactionResult).isNotNull();
    assertThat(withdrawTransactionResult.didTransactionSucceed()).isTrue();
    assertThat(withdrawTransactionResult.getMessage()).isNotNull();
    assertThat(withdrawTransactionResult.getMessage()).isEqualTo(WITHDRAW_SUCCEEDED);
  }

  @Test
  public void testWithdrawAmountWithInvalidTokenShouldFail() throws Exception {
    // given
    Amount hundredDollars = new Amount(HUNDRED_DOLLARS);
    String invalidToken = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, invalidToken))
        .thenReturn(false);

    // when
    TransactionResult withdrawAmountResult = atmMachine.withdrawMoney(invalidToken, testAccount, hundredDollars);

    // then
    assertThat(withdrawAmountResult).isNotNull();
    assertThat(withdrawAmountResult.didTransactionSucceed()).isFalse();
    assertThat(withdrawAmountResult.getMessage()).isNotNull();
    assertThat(withdrawAmountResult.getMessage()).isEqualTo(ATMMachineConstants.INVALID_TOKEN);
  }

  @Test
  public void testWithdrawShouldFailWhenATMDoesNotHaveEnoughCash() throws Exception {
    // given
    Amount thousandDollars = new Amount(THOUSAND_DOLLARS);
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(cashDispenser.getTotalCashInATM())
        .thenReturn(new Amount(HUNDRED_DOLLARS));

    // when
    TransactionResult withdrawTransactionResult = atmMachine.withdrawMoney(token, testAccount, thousandDollars);

    // then
    assertThat(withdrawTransactionResult).isNotNull();
    assertThat(withdrawTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(withdrawTransactionResult.getMessage()).isNotNull();
    assertThat(withdrawTransactionResult.getMessage()).isEqualTo(ATMMachineConstants.INSUFFICIENT_CASH_IN_ATM);
  }

  @Test
  public void testWithdrawShouldFailWhenAccountHasInsufficientBalance() throws Exception {
    // given
    Amount hundredDollars = new Amount(THOUSAND_DOLLARS);
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(cashDispenser.getTotalCashInATM())
        .thenReturn(new Amount(THOUSAND_DOLLARS));
    when(transactionService.getAccountBalance(testAccount))
        .thenReturn(new TransactionResult(true, Double.toString(HUNDRED_DOLLARS)));

    // when
    TransactionResult withdrawTransactionResult = atmMachine.withdrawMoney(token, testAccount, hundredDollars);

    // then
    assertThat(withdrawTransactionResult).isNotNull();
    assertThat(withdrawTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(withdrawTransactionResult.getMessage()).isNotNull();
    assertThat(withdrawTransactionResult.getMessage()).isEqualTo(ATMMachineConstants.INSUFFICIENT_BALANCE_IN_ACCOUNT);
  }

  @Test
  public void testWithdrawAmountShouldFail() throws Exception {
    // given
    Amount hundredDollars = new Amount(HUNDRED_DOLLARS);
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(cashDispenser.getTotalCashInATM())
        .thenReturn(new Amount(THOUSAND_DOLLARS));
    when(transactionService.getAccountBalance(testAccount))
        .thenReturn(new TransactionResult(true, Double.toString(HUNDRED_DOLLARS)));
    when(transactionService.withdrawAmountFromAccount(testAccount, hundredDollars))
        .thenReturn(new TransactionResult(false, WITHDRAW_FAILED));

    // when
    TransactionResult withdrawTransactionResult = atmMachine.withdrawMoney(token, testAccount, hundredDollars);

    // then
    assertThat(withdrawTransactionResult).isNotNull();
    assertThat(withdrawTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(withdrawTransactionResult.getMessage()).isNotNull();
    assertThat(withdrawTransactionResult.getMessage()).isEqualTo(WITHDRAW_FAILED);
  }

  @Test
  public void testWithdrawShouldThrowException() throws Exception {
    // given
    Amount hundredDollars = new Amount(HUNDRED_DOLLARS);
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(cashDispenser.getTotalCashInATM())
        .thenReturn(new Amount(THOUSAND_DOLLARS));
    when(transactionService.getAccountBalance(testAccount))
        .thenReturn(new TransactionResult(true, Double.toString(THOUSAND_DOLLARS)));
    when(transactionService.withdrawAmountFromAccount(testAccount, hundredDollars))
        .thenThrow(new TransactionServiceException(TRANSACTION_SERVICE_EXCEPTION_MESSAGE));

    // when
    TransactionResult depositTransactionResult = atmMachine.withdrawMoney(token, testAccount, hundredDollars);

    // then
    assertThat(depositTransactionResult).isNotNull();
    assertThat(depositTransactionResult.didTransactionSucceed()).isFalse();
    assertThat(depositTransactionResult.getMessage()).isNotNull();
    assertThat(depositTransactionResult.getMessage()).isEqualTo(TRANSACTION_SERVICE_EXCEPTION_MESSAGE);
  }

  @Test
  public void testGetBalanceShouldFailWithInvalidToken() throws Exception {
    // given
    String invalidToken = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, invalidToken))
        .thenReturn(false);

    // when
    TransactionResult transactionResult = atmMachine.getAccountBalance(invalidToken, testAccount);

    // then
    assertThat(transactionResult).isNotNull();
    assertThat(transactionResult.didTransactionSucceed()).isFalse();
    assertThat(transactionResult.getMessage()).isEqualTo(ATMMachineConstants.INVALID_TOKEN);
  }

  @Test
  public void testGetBalanceShouldThrowException() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(transactionService.getAccountBalance(testAccount))
        .thenThrow(new TransactionServiceException(TRANSACTION_SERVICE_EXCEPTION_MESSAGE));

    // when
    TransactionResult transactionResult = atmMachine.getAccountBalance(token, testAccount);

    // then
    assertThat(transactionResult).isNotNull();
    assertThat(transactionResult.didTransactionSucceed()).isFalse();
    assertThat(transactionResult.getMessage()).isEqualTo(TRANSACTION_SERVICE_EXCEPTION_MESSAGE);
  }

  @Test
  public void testGetBalanceShouldSucceed() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(transactionService.getAccountBalance(testAccount))
        .thenReturn(new TransactionResult(true, Double.toString(HUNDRED_DOLLARS)));

    // when
    TransactionResult transactionResult = atmMachine.getAccountBalance(token, testAccount);

    // then
    assertThat(transactionResult).isNotNull();
    assertThat(transactionResult.getMessage()).isEqualTo(Double.toString(HUNDRED_DOLLARS));
  }

  @Test
  public void testLogoutShouldSucceed() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(authenticationService.invalidateToken(token))
        .thenReturn(new AuthenticationResult(true, null));
    when(transactionRecorder.getAllTransactionsByToken(token))
        .thenReturn(new ArrayList<>());

    // when
    AuthenticationResult logoutResult = atmMachine.logout(token, testAccount);

    // then
    assertThat(logoutResult).isNotNull();
    assertThat(logoutResult.getErrorMessage()).isNull();
    assertThat(logoutResult.getToken()).isNull();
    assertThat(logoutResult.authenticted()).isTrue();
  }

  @Test
  public void testLogoutShouldFailWithInvalidToken() throws Exception {
    // given
    String invalidToken = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, invalidToken))
        .thenReturn(false);

    // when
    AuthenticationResult logoutResult = atmMachine.logout(invalidToken, testAccount);

    // then
    assertThat(logoutResult).isNotNull();
    assertThat(logoutResult.authenticted()).isFalse();
    assertThat(logoutResult.getToken()).isNull();
    assertThat(logoutResult.getErrorMessage()).isNotNull();
    assertThat(logoutResult.getErrorMessage()).isEqualTo(ATMMachineConstants.INVALID_TOKEN);
  }

  @Test
  public void testLogoutShouldThrowException() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.isTokenValid(testAccount, token))
        .thenReturn(true);
    when(authenticationService.invalidateToken(token))
        .thenThrow(new AuthenticationServiceException(AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE));

    // when
    AuthenticationResult logoutResult = atmMachine.logout(token, testAccount);

    // then
    assertThat(logoutResult).isNotNull();
    assertThat(logoutResult.getToken()).isNull();
    assertThat(logoutResult.authenticted()).isFalse();
    assertThat(logoutResult.getErrorMessage()).isNotNull();
    assertThat(logoutResult.getErrorMessage()).isEqualTo(AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE);
  }

  private String tokenGenerator() {
    return UUID.randomUUID().toString();
  }

}