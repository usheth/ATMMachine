package atmmachine.domain.model.entities;

import java.util.Map;

public class Cash implements Money {

    private String type = "Cash";
    private Map<Double,Long> denominationCountMap;

    @Override
    public double getMoney() {
        double totalMoney = 0;
        for(Double denomination : denominationCountMap.keySet()) {
            totalMoney += denomination*denominationCountMap.get(denomination);
        }
        return totalMoney;
    }
}
