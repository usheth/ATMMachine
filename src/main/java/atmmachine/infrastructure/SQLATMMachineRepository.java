package atmmachine.infrastructure;

import atmmachine.domain.model.ATMMachineRepository;
import atmmachine.domain.model.Card;
import atmmachine.domain.model.Pin;

public class SQLATMMachineRepository implements ATMMachineRepository {

    @Override
    public Pin getPinByCard(Card card) {
        return null;
    }

}
