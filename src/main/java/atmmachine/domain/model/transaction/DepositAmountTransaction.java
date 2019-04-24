package atmmachine.domain.model.transaction;

import atmmachine.domain.model.entities.Money;

public class DepositAmountTransaction implements Transaction {

    private Money depositedMoney;

    public DepositAmountTransaction(Money depositedMoney) {
        this.depositedMoney = depositedMoney;
    }
}
