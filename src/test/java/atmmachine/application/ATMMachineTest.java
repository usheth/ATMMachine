package atmmachine.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.TransactionRecorder;
import atmmachine.domain.model.TransactionService;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.CardType;
import atmmachine.domain.model.entities.Cash;
import atmmachine.domain.model.entities.Customer;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.entities.SavingsAccount;
import atmmachine.domain.model.exception.AuthenticationServiceException;
import atmmachine.domain.model.exception.TransactionServiceException;
import atmmachine.domain.model.transaction.TransactionResult;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class ATMMachineTest {

  private ATMMachine atmMachine;
  private Account testAccount;
  private Customer testCustomer;
  @Mock private AuthenticationService authenticationService;
  @Mock private TransactionService transactionService;
  @Mock private TransactionRecorder transactionRecorder;
  private static final String INVALID_CREDENTIALS = "Invalid Credentials";
  private static final String AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE = "Could not connect to database";
  private static final String TRANSACTION_SERVICE_EXCEPTION_MESSAGE = "The world is on fire";
  private static final String DEPOSIT_SUCCEEDED = "Congratulations";
  private static final String DEPOSIT_FAILED = "Dammit";

  @BeforeMethod
  public void before() {
    MockitoAnnotations.initMocks(this);
    atmMachine = new ATMMachine(authenticationService, transactionService, transactionRecorder);
    testAccount = new SavingsAccount();
    testCustomer = new Customer();
  }

  @Test
  public void testVerificationOfCredentialsShouldPass() throws Exception {
    // given
    String token = tokenGenerator();
    when(authenticationService.authenticate(any(Card.class), any(Pin.class)))
        .thenReturn(new AuthenticationResult(true, token));
    Card card = new Card(CardType.MASTER_CARD, "123", new Customer());
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
    Card card = new Card(CardType.MASTER_CARD, "123", new Customer());
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
    Card card = new Card(CardType.MASTER_CARD, "123", new Customer());
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
    when(authenticationService.isTokenValid(testAccount, token)).thenReturn(true);
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

  private String tokenGenerator() {
    return UUID.randomUUID().toString();
  }

}