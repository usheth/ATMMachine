package atmmachine.domain.model.entities;

public class AuthenticationRequest {

  private Card card;
  private Pin pin;

  public Card getCard() {
    return card;
  }

  public Pin getPin() {
    return pin;
  }
}
