package atmmachine.domain.model;

import atmmachine.domain.model.entities.*;
import atmmachine.domain.model.transaction.TransactionResult;

public interface ATMMachineRepository {

    //gets the PIN associated with a card
    Pin getPinByCard(Card card);

    //gets the account a card belongs to
    Account getAccountByCard(Card card);

    //gets the customer a card belongs to
    Customer getCustomerByCard(Card card);

    //blocks until the calling thread has a write lock on the account
    void getWriteLockOnAccount(Account account);

    //called by a thread to release a write lock on the account
    void releaseWriteLockOnAccount(Account account);

    //adds money to the account, internally calls getMoney on the Money object
    TransactionResult addMoneyToAccount(Account account, Money money);

    //subtracts the given amount from the account
    TransactionResult subtractAmountFromAccount(Account account, Amount amount);

    //check if the session is correctly registered with our repository, is open, other checks
    boolean isTokenValid(Account account, String token);

    //gets the balance in the account
    Amount getAccountBalance(Account account);

    //blocks until the calling thread has a read lock on the account
    void getReadLockOnAccount(Account account);

    //called by a thread to release a read lock on the account
    void releaseReadLockOnAccount(Account account);

    //authenticates a card given a pin
    boolean authenticateCardFromPin(Card card, Pin pin);

    //disables a card
    void handleFailedAuthenticationForCard(Card card);

    AuthenticationResult logout(String token);

    String createAccessTokenForCard(Card card);

}
