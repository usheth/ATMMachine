package atmmachine.domain.model.entities;

public class Check implements Money {
    @Override
    public double getMoney() {
        //Do OCR or something on the check
        return 0;
    }
}
