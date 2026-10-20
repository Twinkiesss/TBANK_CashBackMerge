package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.cli.BoardPrinter;
import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.SpawnRule;
import academy.cashbackmerge.model.GameState;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR1. Воспроизводимость по seed")
class SeedTest {

    private static final long SEED = 42L;

    private static CampaignConfig config() {
        return new CampaignConfig(
                4,
                100,
                List.of(
                        new SpawnRule(new BigDecimal("1"), new BigDecimal("0.85")),
                        new SpawnRule(new BigDecimal("2"), new BigDecimal("0.15"))),
                List.of(),
                "32",
                "0.20");
    }

    @Test
    @DisplayName("Одинаковый конфиг и seed дают одинаковое начальное поле")
    void sameConfigAndSeedProduceSameInitialBoard() {
        GameState a = GameState.createInitial(config(), SEED);
        GameState b = GameState.createInitial(config(), SEED);

        assertThat(BoardPrinter.print(a.getBoard())).isEqualTo(BoardPrinter.print(b.getBoard()));
    }

    @Test
    @DisplayName("Одинаковый конфиг, seed и стратегия дают одинаковый отчёт симуляции")
    void sameInputsProduceSameSimulationReport() {
        academy.cashbackmerge.simulation.Simulator simulator = new academy.cashbackmerge.simulation.Simulator(config());

        var result1 = simulator.simulate(5, () -> new academy.cashbackmerge.simulation.RandomStrategy(SEED));
        var result2 = simulator.simulate(5, () -> new academy.cashbackmerge.simulation.RandomStrategy(SEED));

        assertThat(result1.averageScore()).isEqualTo(result2.averageScore());
        assertThat(result1.tileDistribution()).isEqualTo(result2.tileDistribution());
    }
}
