package atmmachine.domain.model.entities;

public class Amount {

    private String type = "Amount";
    private double amount;

    public Amount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
