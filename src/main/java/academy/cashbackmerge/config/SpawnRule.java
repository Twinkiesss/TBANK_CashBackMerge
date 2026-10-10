package academy.cashbackmerge.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record SpawnRule(BigDecimal cashback, BigDecimal probability) {

    @JsonCreator
    public SpawnRule(@JsonProperty("cashback") String cashback, @JsonProperty("probability") String probability) {
        this(new BigDecimal(cashback), new BigDecimal(probability));
    }
}
