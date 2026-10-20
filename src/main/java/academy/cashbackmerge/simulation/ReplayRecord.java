package academy.cashbackmerge.simulation;

import academy.cashbackmerge.model.Direction;
import java.util.List;

public record ReplayRecord(long seed, List<Direction> moves) {}
