package academy.cashbackmerge.simulation;

import academy.cashbackmerge.config.CampaignConfig;

public final class CompareSimulator {

    private static final int PERCENT_MULTIPLIER = 100;

    private CompareSimulator() {}

    public static String compare(CampaignConfig config1, CampaignConfig config2, int players, long seed) {
        Simulator sim1 = new Simulator(config1);
        Simulator sim2 = new Simulator(config2);

        SimulationResult res1 = sim1.simulate(players, () -> new RandomStrategy(seed));
        SimulationResult res2 = sim2.simulate(players, () -> new RandomStrategy(seed));

        return String.format(
                """
            # Сравнение кампаний

            | Параметр | Кампания 1 | Кампания 2 |
            |---|---|---|
            | Лимит ходов | %d | %d |
            | Средний Score | %.2f | %.2f |
            | Доля макс. награды | %.2f%% | %.2f%% |
            | Статус | %s | %s |
            """,
                config1.movesLimit(),
                config2.movesLimit(),
                res1.averageScore(),
                res2.averageScore(),
                res1.actualMaxRewardRate() * PERCENT_MULTIPLIER,
                res2.actualMaxRewardRate() * PERCENT_MULTIPLIER,
                res1.isOk() ? "OK" : "FAILED",
                res2.isOk() ? "OK" : "FAILED");
    }
}
