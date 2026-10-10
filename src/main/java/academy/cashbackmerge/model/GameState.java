package academy.cashbackmerge.model;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.SpawnRule;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import lombok.Getter;

@Getter
public final class GameState {

    private final Board board;
    private final int movesLimit;
    private final Random rng;
    private int movesUsed;
    private long score;

    public GameState(Board board, int movesLimit, Random rng) {
        this.board = board;
        this.movesLimit = movesLimit;
        this.rng = rng;
    }

    public static GameState createInitial(CampaignConfig config, long seed) {
        Board board = new Board(config.boardSize());
        GameState state = new GameState(board, config.movesLimit(), new Random(seed));
        state.spawnTile(config.spawnRules());
        state.spawnTile(config.spawnRules());
        return state;
    }

    public void spawnTile(List<SpawnRule> rules) {
        List<Cell> empty = board.emptyCells();
        if (empty.isEmpty()) {
            return;
        }
        Cell cell = empty.get(rng.nextInt(empty.size()));
        Tile tile = pickTile(rules);
        board.setTile(cell.row(), cell.col(), tile);
    }

    private Tile pickTile(List<SpawnRule> rules) {
        BigDecimal r = BigDecimal.valueOf(rng.nextDouble());
        BigDecimal acc = BigDecimal.ZERO;
        for (SpawnRule rule : rules) {
            acc = acc.add(rule.probability());
            if (r.compareTo(acc) < 0) {
                return new Tile(rule.cashback());
            }
        }
        return new Tile(rules.get(rules.size() - 1).cashback());
    }
}
