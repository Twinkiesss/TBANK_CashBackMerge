package academy.cashbackmerge.simulation;

import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import java.util.Random;

public final class RandomStrategy implements Strategy {

    private final Random rng;

    public RandomStrategy(long seed) {
        this.rng = new Random(seed);
    }

    @Override
    public Direction chooseMove(GameState state) {
        Direction[] directions = Direction.values();
        return directions[rng.nextInt(directions.length)];
    }
}
