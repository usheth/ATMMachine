package atmmachine.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
import org.testng.annotations.Test;

public class ATMMachineTest {

  private ATMMachine atmMachine;

  @Test
  public void testVerificationOfCredentialsShouldPass() {
    // given
    AuthenticationService authenticationService = mock(AuthenticationService.class);
    // TODO :  We're dictating how the service should behave in this test rather than actually testing it (?)
    when(authenticationService.authenticate(any(Card.class), any(Pin.class)))
        .thenReturn(new AuthenticationResult(false, "Invalid Credentials"));
    TransactionService transactionService = mock(TransactionService.class);
    TransactionRecorder transactionRecorder = mock(TransactionRecorder.class);
    atmMachine = new ATMMachine(authenticationService, transactionService, transactionRecorder);
    Card card = new Card(CardType.MASTER_CARD, "123", new Customer());
    Pin pin = new Pin();

    // when
    AuthenticationResult result = atmMachine.verifyCredentials(card, pin);

    // then
    //verify(atmMachine).verifyCredentials(card, pin);
    assertThat(result).isNotNull();
    assertThat(result.authenticted()).isTrue();
    assertThat(result.getToken()).isNotNull();
    assertThat(result.getErrorMessage()).isNull();
  }

}