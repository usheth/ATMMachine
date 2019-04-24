package atmmachine.domain.model.transaction;

import atmmachine.domain.model.entities.Money;

public class WithdrawAmountTransaction implements Transaction{

    private Money withdrawnMoney;

    public WithdrawAmountTransaction(Money withdrawnMoney) {
        this.withdrawnMoney = withdrawnMoney;
    }
}
