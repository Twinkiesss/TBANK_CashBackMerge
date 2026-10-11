package academy.cashbackmerge.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.MoveResult;
import academy.cashbackmerge.model.Tile;
import academy.cashbackmerge.support.BoardFixtures;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR2. Движок ходов")
class MoveEngineTest {

    private static final List<SpawnRule> ONE_RULE = List.of(new SpawnRule(new BigDecimal("1"), BigDecimal.ONE));

    private static GameState stateWith(Board board, int movesLimit) {
        return new GameState(board, movesLimit, ONE_RULE, new Random(42L));
    }

    @Test
    @DisplayName("Невалидный ход не меняет поле, не тратит ход и не создаёт плитку")
    void invalidMoveChangesNothing() {
        Board board = new Board(4);
        BoardFixtures.fillRow(board, 0, "2,4,.,.");
        GameState state = stateWith(board, 10);
        int occupiedBefore = board.size() * board.size() - board.emptyCells().size();

        MoveResult result = state.applyMove(Direction.LEFT);

        assertThat(result.changed()).isFalse();
        assertThat(state.getMovesUsed()).isZero();
        assertThat(BoardFixtures.readRow(board, 0)).isEqualTo("2,4,.,.");
        int occupiedAfter = board.size() * board.size() - board.emptyCells().size();
        assertThat(occupiedAfter).isEqualTo(occupiedBefore);
    }

    @Test
    @DisplayName("После валидного хода появляется ровно одна новая плитка")
    void validMoveSpawnsExactlyOneTile() {
        Board board = new Board(4);
        BoardFixtures.fillRow(board, 0, "2,2,.,.");
        GameState state = stateWith(board, 10);
        int occupiedBefore = board.size() * board.size() - board.emptyCells().size();

        MoveResult result = state.applyMove(Direction.LEFT);

        assertThat(result.changed()).isTrue();
        int occupiedAfter = board.size() * board.size() - board.emptyCells().size();
        // было две плитки, одна слилась в одну (занято 1), плюс одна новая = 2
        assertThat(occupiedAfter).isEqualTo(occupiedBefore - 2 + 1 + 1);
        assertThat(state.getMovesUsed()).isEqualTo(1);
    }

    @Test
    @DisplayName("После N валидных ходов игра завершена")
    void gameEndsAfterMovesLimit() {
        Board board = new Board(4);
        board.setTile(0, 0, new Tile(new BigDecimal("2")));
        board.setTile(0, 1, new Tile(new BigDecimal("2")));
        board.setTile(1, 0, new Tile(new BigDecimal("4")));
        board.setTile(1, 1, new Tile(new BigDecimal("4")));
        board.setTile(2, 0, new Tile(new BigDecimal("8")));
        board.setTile(2, 1, new Tile(new BigDecimal("8")));
        board.setTile(3, 0, new Tile(new BigDecimal("16")));
        board.setTile(3, 1, new Tile(new BigDecimal("16")));
        GameState state = stateWith(board, 1);

        state.applyMove(Direction.LEFT);

        assertThat(state.isGameOver()).isTrue();
        assertThat(state.getMovesUsed()).isEqualTo(1);
    }

    @Test
    @DisplayName("Достижение плитки 32% не заканчивает игру — доигрываем до лимита ходов")
    void reachingMaxTileDoesNotEndGame() {
        Board board = new Board(4);
        board.setTile(0, 0, new Tile(new BigDecimal("16")));
        board.setTile(0, 1, new Tile(new BigDecimal("16")));
        BoardFixtures.fillRow(board, 1, "2,.,.,.");
        BoardFixtures.fillRow(board, 2, "2,.,.,.");
        BoardFixtures.fillRow(board, 3, "2,.,.,.");
        GameState state = stateWith(board, 5);

        state.applyMove(Direction.LEFT);

        assertThat(state.getBoard().tileAt(0, 0).orElseThrow().cashback()).isEqualByComparingTo("32");
        assertThat(state.isGameOver()).isFalse();
        assertThat(state.getMovesUsed()).isEqualTo(1);
    }

    @Test
    @DisplayName("Score считается по правилам и растёт только при слияниях")
    void scoreIsCalculatedOnMerges() {
        Board board = new Board(4);
        BoardFixtures.fillRow(board, 0, "2,2,4,4");
        GameState state = stateWith(board, 10);

        MoveResult result = state.applyMove(Direction.LEFT);

        // 2+2=4 (score +4), 4+4=8 (score +8), итого 12
        assertThat(result.scoreGained()).isEqualTo(12L);
        assertThat(state.getScore()).isEqualTo(12L);
    }
}
