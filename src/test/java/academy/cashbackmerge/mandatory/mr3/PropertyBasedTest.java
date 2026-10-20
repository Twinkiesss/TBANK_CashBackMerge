package academy.cashbackmerge.mandatory.mr3;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.Tile;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

@DisplayName("Бонус. Генеративные тесты (Property-based)")
class PropertyBasedTest {

    @RepeatedTest(50)
    @DisplayName("Сумма плиток не уменьшается после хода (сохраняется закон сохранения массы)")
    void sumOfTilesNeverDecreases() {
        Random rng = new Random();
        Board board = new Board(4);

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (rng.nextBoolean()) {
                    int power = rng.nextInt(4) + 1; // 1, 2, 4, 8
                    board.setTile(r, c, new Tile(BigDecimal.valueOf(Math.pow(2, power))));
                }
            }
        }

        long sumBefore = getSum(board);

        CampaignConfig config = new CampaignConfig(
                4, 10, List.of(new SpawnRule(new BigDecimal("1"), BigDecimal.ONE)), List.of(), "32", "0.20");
        GameState state = new GameState(board, 10, config.spawnRules(), rng);

        state.applyMove(Direction.LEFT);

        long sumAfter = getSum(board);

        assertThat(sumAfter).isGreaterThanOrEqualTo(sumBefore);
    }

    private long getSum(Board board) {
        long sum = 0;
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.size(); c++) {
                sum += board.tileAt(r, c).map(t -> t.cashback().longValue()).orElse(0L);
            }
        }
        return sum;
    }
}
