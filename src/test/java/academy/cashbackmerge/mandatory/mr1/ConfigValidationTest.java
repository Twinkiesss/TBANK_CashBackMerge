package academy.cashbackmerge.mandatory.mr1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.config.SpawnRule;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MR1. Конфиг кампании")
class ConfigValidationTest {

    private static CampaignConfig config(int movesLimit, List<SpawnRule> spawnRules) {
        return new CampaignConfig(4, movesLimit, spawnRules, List.of(), "32", "0.20");
    }

    @Test
    @DisplayName("Валидный конфиг принимается")
    void validConfigIsAccepted() {
        CampaignConfig config = config(
                10,
                List.of(
                        new SpawnRule(new BigDecimal("1"), new BigDecimal("0.85")),
                        new SpawnRule(new BigDecimal("2"), new BigDecimal("0.15"))));

        ConfigLoader.validate(config);

        assertThat(config.movesLimit()).isEqualTo(10);
        assertThat(config.boardSize()).isEqualTo(4);
    }

    @Test
    @DisplayName("Сумма вероятностей spawn-правил не равна 1 — конфиг отклоняется")
    void probabilitiesNotSummingToOneAreRejected() {
        CampaignConfig config = config(
                10,
                List.of(
                        new SpawnRule(new BigDecimal("1"), new BigDecimal("0.85")),
                        new SpawnRule(new BigDecimal("2"), new BigDecimal("0.10"))));

        assertThatThrownBy(() -> ConfigLoader.validate(config))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Сумма вероятностей");
    }

    @Test
    @DisplayName("movesLimit <= 0 — конфиг отклоняется")
    void nonPositiveMovesLimitIsRejected() {
        CampaignConfig config = config(0, List.of(new SpawnRule(new BigDecimal("1"), new BigDecimal("1"))));

        assertThatThrownBy(() -> ConfigLoader.validate(config))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("movesLimit");
    }

    @Test
    @DisplayName("Пустой список spawn-правил — конфиг отклоняется")
    void emptySpawnRulesAreRejected() {
        CampaignConfig config = config(10, List.of());

        assertThatThrownBy(() -> ConfigLoader.validate(config))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("spawnRules");
    }
}
