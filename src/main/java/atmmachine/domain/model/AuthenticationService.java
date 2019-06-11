package atmmachine.domain.model;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.exception.AuthenticationServiceException;

public interface AuthenticationService {

  /**
   * This method is used to identify a user using a card and a pin code.
   *
   * @param card Card
   * @param pin Pin
   * @return {@code AuthenticationResult} Authentication result
   */
  AuthenticationResult authenticate(Card card, Pin pin) throws AuthenticationServiceException;

  /**
   * This method checks if a authentication token is valid and matches the account.
   *
   * @param account Account
   * @param token Token
   * @return {@code boolean} true if the token is valid
   */
  boolean isTokenValid(long accountId, String token) throws AuthenticationServiceException;

  /**
   * This method invalidates the authentication token associated with an account
   *
   * @param token Token
   * @return {@code AuthenticationResult} Authentication result
   */
  AuthenticationResult invalidateToken(String token) throws AuthenticationServiceException;
}
