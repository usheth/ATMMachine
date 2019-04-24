package atmmachine.domain.model;

import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.entities.Session;

public interface ATMMachineRepository {

    Pin getPinByCard(Card card);
    Account getAccountByCard(Card card);
    Session openSessionForAccount(Account account);

}
