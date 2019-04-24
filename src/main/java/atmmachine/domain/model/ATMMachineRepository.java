package atmmachine.domain.model;

public interface ATMMachineRepository {

    Pin getPinByCard(Card card);

}
