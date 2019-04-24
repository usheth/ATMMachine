package atmmachine.infrastructure;

import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;

public class CashDispenser {

    private static CashDispenser instance;

    public static synchronized CashDispenser getInstance() {
        if(instance == null) {
            instance = new CashDispenser();
        }
        return instance;
    }

    public Amount getTotalCashInATM() {
        return null;
    }

    public Money dispenseAmountAndGetDenominations(Amount amount) {
        //let the cash dispenser figure out denominations etc
        return null;
    }

}
