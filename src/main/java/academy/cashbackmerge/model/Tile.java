package academy.cashbackmerge.model;

import java.math.BigDecimal;

public record Tile(BigDecimal cashback) {

    public Tile {
        if (cashback.signum() <= 0) {
            throw new IllegalArgumentException("cashback должен быть > 0");
        }
    }

    public Tile merged() {
        return new Tile(cashback.multiply(BigDecimal.valueOf(2)));
    }

    @Override
    public String toString() {
        return cashback.toPlainString() + "%";
    }
}
