package atmmachine.domain.model.transaction;

import atmmachine.domain.model.entities.Amount;
import atmmachine.domain.model.entities.Money;

public class WithdrawAmountTransaction implements Transaction{

    private Amount withdrawnMoney;

    public WithdrawAmountTransaction(Amount withdrawnMoney) {
        this.withdrawnMoney = withdrawnMoney;
    }
}
