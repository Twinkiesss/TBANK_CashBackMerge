package academy.cashbackmerge.simulation;

import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;

public final class GreedyStrategy implements Strategy {

    @Override
    public Direction chooseMove(GameState state) {
        Direction bestMove = Direction.LEFT;
        int maxMerges = -1;

        for (Direction dir : Direction.values()) {
            int merges = state.getBoard().countMerges(dir);
            if (merges > maxMerges) {
                maxMerges = merges;
                bestMove = dir;
            }
        }
        return bestMove;
    }
}
