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
    private final List<SpawnRule> spawnRules;
    private final Random rng;
    private int movesUsed;
    private long score;

    public GameState(Board board, int movesLimit, List<SpawnRule> spawnRules, Random rng) {
        this.board = board;
        this.movesLimit = movesLimit;
        this.spawnRules = spawnRules;
        this.rng = rng;
    }

    public static GameState createInitial(CampaignConfig config, long seed) {
        Board board = new Board(config.boardSize());
        GameState state = new GameState(board, config.movesLimit(), config.spawnRules(), new Random(seed));
        state.spawnTile();
        state.spawnTile();
        return state;
    }

    public boolean isGameOver() {
        return movesUsed >= movesLimit || !board.canMove();
    }

    public MoveResult applyMove(Direction direction) {
        if (isGameOver()) {
            return MoveResult.invalid();
        }

        long gained = board.move(direction);

        if (gained < 0) {
            return MoveResult.invalid();
        }

        score += gained;
        movesUsed++;
        spawnTile();
        return MoveResult.valid(gained);
    }

    public void spawnTile() {
        List<Cell> empty = board.emptyCells();
        if (empty.isEmpty()) {
            return;
        }
        Cell cell = empty.get(rng.nextInt(empty.size()));
        Tile tile = pickTile();
        board.setTile(cell.row(), cell.col(), tile);
    }

    private Tile pickTile() {
        BigDecimal r = BigDecimal.valueOf(rng.nextDouble());
        BigDecimal acc = BigDecimal.ZERO;
        for (SpawnRule rule : spawnRules) {
            acc = acc.add(rule.probability());
            if (r.compareTo(acc) < 0) {
                return new Tile(rule.cashback());
            }
        }
        return new Tile(spawnRules.get(spawnRules.size() - 1).cashback());
    }
}
