package academy.cashbackmerge.simulation;

import java.math.BigDecimal;
import java.util.Map;

public record SimulationResult(
        Map<BigDecimal, Integer> tileDistribution,
        Map<BigDecimal, Integer> rewardDistribution,
        double averageScore,
        long maxScore,
        double actualMaxRewardRate,
        boolean isOk) {}
