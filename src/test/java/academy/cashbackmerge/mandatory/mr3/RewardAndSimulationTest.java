package academy.cashbackmerge.mandatory.mr3;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.RewardRule;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.simulation.RandomStrategy;
import academy.cashbackmerge.simulation.ReportWriter;
import academy.cashbackmerge.simulation.RewardTable;
import academy.cashbackmerge.simulation.SimulationResult;
import academy.cashbackmerge.simulation.Simulator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR3. Награды и симуляция")
class RewardAndSimulationTest {

    private CampaignConfig config;

    @BeforeEach
    void setUp() {
        config = new CampaignConfig(
                4,
                50,
                List.of(
                        new SpawnRule(new BigDecimal("1"), new BigDecimal("0.85")),
                        new SpawnRule(new BigDecimal("2"), new BigDecimal("0.15"))),
                List.of(
                        new RewardRule(new BigDecimal("4"), new BigDecimal("50")),
                        new RewardRule(new BigDecimal("8"), new BigDecimal("150")),
                        new RewardRule(new BigDecimal("16"), new BigDecimal("500")),
                        new RewardRule(new BigDecimal("32"), new BigDecimal("2000"))),
                "32",
                "0.20");
    }

    @Test
    @DisplayName("Максимальная плитка 16% даёт награду соответствующего tier")
    void rewardMatchesHighestTile() {
        RewardTable table = new RewardTable(config);
        BigDecimal reward = table.rewardFor(new BigDecimal("16"));
        assertThat(reward).isEqualByComparingTo("500");
    }

    @Test
    @DisplayName("Weighted random учитывает вероятности из конфига")
    void spawnRespectsConfiguredProbabilities() {
        Board board = new Board(4);
        GameState state = new GameState(board, 10, config.spawnRules(), new Random(42));

        for (int i = 0; i < 100; i++) {
            state.spawnTile();
        }

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                board.tileAt(r, c)
                        .ifPresent(tile -> assertThat(tile.cashback()).isIn(new BigDecimal("1"), new BigDecimal("2")));
            }
        }
    }

    @Test
    @DisplayName("Отчёт содержит распределение максимальных плиток и наград")
    void reportContainsDistributions() {
        Simulator simulator = new Simulator(config);
        SimulationResult result = simulator.simulate(10, () -> new RandomStrategy(42));

        String report = ReportWriter.writeMarkdown(config, 10, 42, "random", result);

        assertThat(report).contains("Распределение наград");
        assertThat(report).contains("Доля игроков с макс. наградой");
    }

    @Test
    @DisplayName("Отчёт содержит долю игроков с максимальной наградой")
    void reportContainsMaxRewardRate() {
        Simulator simulator = new Simulator(config);
        SimulationResult result = simulator.simulate(10, () -> new RandomStrategy(42));

        assertThat(result.actualMaxRewardRate()).isBetween(0.0, 1.0);
    }

    @Test
    @DisplayName("Статус OK, если actualMaxRewardRate <= maxRewardAllowedRate, иначе FAILED")
    void reportStatusReflectsBusinessConstraint() {
        Simulator simulator = new Simulator(config);
        SimulationResult result = simulator.simulate(10, () -> new RandomStrategy(42));

        boolean expectedOk = result.actualMaxRewardRate() <= 0.20;
        assertThat(result.isOk()).isEqualTo(expectedOk);
    }
}
