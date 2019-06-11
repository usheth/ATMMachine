package atmmachine.domain.model.entities;

public class Amount {

    private double value;

    public Amount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
