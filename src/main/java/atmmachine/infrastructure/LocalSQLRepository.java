package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.AuthenticationResult;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Customer;
import atmmachine.domain.model.entities.Money;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.transaction.TransactionResult;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocalSQLRepository implements ATMMachineRepository {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Pin getPinByCard(Card card) {
    return null;
  }

  @Override
  public Account getAccountByCard(Card card) {
    return null;
  }

  @Override
  public Customer getCustomerByCard(Card card) {
    return null;
  }

  @Override
  public void getWriteLockOnAccount(long accountId) {

  }

  @Override
  public void releaseWriteLockOnAccount(long accountId) {

  }

  @Override
  public TransactionResult addMoneyToAccount(long accountId, Money money) {
    return null;
  }

  @Override
  public TransactionResult subtractAmountFromAccount(long accountId, Amount amount) {
    return null;
  }

  @Override
  public boolean isTokenValid(long accountId, String token) {
    return false;
  }

  @Override
  public Amount getAccountBalance(long accountId) {
    return null;
  }

  @Override
  public void getReadLockOnAccount(long accountId) {

  }

  @Override
  public void releaseReadLockOnAccount(long accountId) {

  }

  @Override
  public boolean authenticateCardFromPin(Card card, Pin pin) {
    return false;
  }

  @Override
  public void handleFailedAuthenticationForCard(Card card) {

  }

  @Override
  public AuthenticationResult logout(String token) {
    return null;
  }

  @Override
  public String createAccessTokenForCard(Card card) {
    return null;
  }
}
