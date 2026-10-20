package academy.cashbackmerge.simulation;

import academy.cashbackmerge.config.CampaignConfig;
import java.math.BigDecimal;
import java.util.Map;

public final class ReportWriter {

    private static final int PERCENT_MULTIPLIER = 100;

    private ReportWriter() {}

    public static String writeMarkdown(
            CampaignConfig config, int players, long seed, String strategy, SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Отчёт симуляции Cashback Merge\n\n");
        sb.append("## Параметры\n\n");
        sb.append("- **Игроков:** ").append(players).append("\n");
        sb.append("- **Seed:** ").append(seed).append("\n");
        sb.append("- **Стратегия:** ").append(strategy).append("\n");
        sb.append("- **Лимит ходов:** ").append(config.movesLimit()).append("\n\n");

        sb.append("## Результаты\n\n");
        sb.append("- **Средний score:** ")
                .append(String.format("%.2f", result.averageScore()))
                .append("\n");
        sb.append("- **Максимальный score:** ").append(result.maxScore()).append("\n");
        sb.append("- **Доля игроков с макс. наградой:** ")
                .append(String.format("%.2f%%", result.actualMaxRewardRate() * PERCENT_MULTIPLIER))
                .append("\n\n");

        sb.append("## Распределение наград\n\n");
        for (Map.Entry<BigDecimal, Integer> entry : result.rewardDistribution().entrySet()) {
            sb.append("- ")
                    .append(entry.getKey().toPlainString())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }
        sb.append("\n");

        sb.append("## Статус\n\n");
        sb.append(result.isOk() ? "**OK**" : "**FAILED**").append("\n");

        return sb.toString();
    }
}
