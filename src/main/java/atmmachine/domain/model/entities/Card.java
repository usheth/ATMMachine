package atmmachine.domain.model.entities;

public class Card {

    private String type = "Card";
    private CardType cardType;
    private String cvv;
    private Customer customer;

    public Card(CardType cardType, String cvv, Customer customer) {
        this.cardType = cardType;
        this.cvv = cvv;
        this.customer = customer;
    }
}
