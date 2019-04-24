package atmmachine.infrastructure;

import atmmachine.domain.model.*;
import atmmachine.domain.model.entities.Account;
import atmmachine.domain.model.entities.Card;
import atmmachine.domain.model.entities.Pin;
import atmmachine.domain.model.entities.Session;

public class SQLATMMachineRepository implements ATMMachineRepository {

    @Override
    public Pin getPinByCard(Card card) {
        return null;
    }

    @Override
    public Account getAccountByCard(Card card) {
        return null;
    }

    @Override
    public Session openSessionForAccount(Account account) {
        return null;
    }

}
