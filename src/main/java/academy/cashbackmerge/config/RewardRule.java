package academy.cashbackmerge.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RewardRule(BigDecimal cashback, BigDecimal reward) {

    @JsonCreator
    public RewardRule(@JsonProperty("cashback") String cashback, @JsonProperty("reward") String reward) {
        this(new BigDecimal(cashback), new BigDecimal(reward));
    }
}
