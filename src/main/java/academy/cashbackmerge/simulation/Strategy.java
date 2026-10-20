package academy.cashbackmerge.simulation;

import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;

public interface Strategy {
    Direction chooseMove(GameState state);
}
