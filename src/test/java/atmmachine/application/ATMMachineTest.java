package atmmachine.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.AuthenticationService;
import atmmachine.domain.model.TransactionRecorder;
import atmmachine.domain.model.TransactionService;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.CardType;
import atmmachine.domain.model.entities.Customer;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.exception.ATMMachineException;
import atmmachine.domain.model.exception.AuthenticationServiceException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

public class ATMMachineTest {

  private ATMMachine atmMachine;
  @Mock private AuthenticationService authenticationService;
  @Mock private TransactionService transactionService;
  @Mock private TransactionRecorder transactionRecorder;
  private static final String INVALID_CREDENTIALS = "Invalid Credentials";
  private static final String AUTHENTICATION_SERVICE_EXCEPTION_MESSAGE = "Could not connect to database";

  @BeforeMethod
  public void before() {
    MockitoAnnotations.initMocks(this);
    atmMachine = new ATMMachine(authenticationService, transactionService, transactionRecorder);
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

  private String tokenGenerator() {
    return UUID.randomUUID().toString();
  }

}