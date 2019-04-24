package atmmachine.domain.model.entities;

import java.util.Objects;

public class Pin {

    private String type = "Pin";
    private String pin;

    public boolean doesPinMatch(Pin pin) {
        return this.equals(pin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pin)) return false;
        Pin pin1 = (Pin) o;
        return Objects.equals(pin, pin1.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pin);
    }
}
