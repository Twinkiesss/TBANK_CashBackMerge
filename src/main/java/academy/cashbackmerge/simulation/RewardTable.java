package academy.cashbackmerge.simulation;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.RewardRule;
import academy.cashbackmerge.model.Board;
import java.math.BigDecimal;

public final class RewardTable {

    private final CampaignConfig config;

    public RewardTable(CampaignConfig config) {
        this.config = config;
    }

    public BigDecimal maxTile(Board board) {
        BigDecimal max = BigDecimal.ZERO;
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.size(); c++) {
                BigDecimal val = board.tileAt(r, c).map(t -> t.cashback()).orElse(BigDecimal.ZERO);
                if (val.compareTo(max) > 0) {
                    max = val;
                }
            }
        }
        return max;
    }

    public BigDecimal rewardFor(BigDecimal cashback) {
        for (RewardRule rule : config.rewardTable()) {
            if (rule.cashback().compareTo(cashback) == 0) {
                return rule.reward();
            }
        }
        return BigDecimal.ZERO; // если такой плитки нет в таблице
    }
}
