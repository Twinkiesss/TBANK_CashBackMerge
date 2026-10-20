package academy.cashbackmerge.simulation;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Simulator {

    private final CampaignConfig config;
    private final RewardTable rewardTable;

    public Simulator(CampaignConfig config) {
        this.config = config;
        this.rewardTable = new RewardTable(config);
    }

    public SimulationResult simulate(int players, Supplier<Strategy> strategySupplier) {
        Map<BigDecimal, Integer> tileDist = new HashMap<>();
        Map<BigDecimal, Integer> rewardDist = new HashMap<>();
        long totalScore = 0;
        long maxScore = 0;
        int maxRewardCount = 0;

        BigDecimal targetMaxCashback = new BigDecimal(config.maxRewardCashback());
        BigDecimal maxReward = rewardTable.rewardFor(targetMaxCashback);
        BigDecimal allowedRate = new BigDecimal(config.maxRewardAllowedRate());

        for (int i = 0; i < players; i++) {
            GameState state = GameState.createInitial(config, i); // уникальный seed для каждого игрока
            Strategy strategy = strategySupplier.get();

            while (!state.isGameOver()) {
                Direction dir = strategy.chooseMove(state);
                state.applyMove(dir);
            }

            BigDecimal maxTile = rewardTable.maxTile(state.getBoard());
            BigDecimal reward = rewardTable.rewardFor(maxTile);

            tileDist.merge(maxTile, 1, Integer::sum);
            rewardDist.merge(reward, 1, Integer::sum);

            totalScore += state.getScore();
            if (state.getScore() > maxScore) {
                maxScore = state.getScore();
            }

            if (reward.compareTo(maxReward) == 0) {
                maxRewardCount++;
            }
        }

        double avgScore = (double) totalScore / players;
        double actualMaxRewardRate = (double) maxRewardCount / players;
        boolean isOk = actualMaxRewardRate <= allowedRate.doubleValue();

        return new SimulationResult(tileDist, rewardDist, avgScore, maxScore, actualMaxRewardRate, isOk);
    }
}
