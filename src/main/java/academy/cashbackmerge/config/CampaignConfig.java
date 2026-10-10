package academy.cashbackmerge.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaignConfig(
        int boardSize,
        int movesLimit,
        List<SpawnRule> spawnRules,
        List<RewardRule> rewardTable,
        String maxRewardCashback,
        String maxRewardAllowedRate) {

    @JsonCreator
    public CampaignConfig(
            @JsonProperty("boardSize") int boardSize,
            @JsonProperty("movesLimit") int movesLimit,
            @JsonProperty("spawnRules") List<SpawnRule> spawnRules,
            @JsonProperty("rewardTable") List<RewardRule> rewardTable,
            @JsonProperty("maxRewardCashback") String maxRewardCashback,
            @JsonProperty("maxRewardAllowedRate") String maxRewardAllowedRate) {
        this.boardSize = boardSize;
        this.movesLimit = movesLimit;
        this.spawnRules = spawnRules == null ? List.of() : List.copyOf(spawnRules);
        this.rewardTable = rewardTable == null ? List.of() : List.copyOf(rewardTable);
        this.maxRewardCashback = maxRewardCashback;
        this.maxRewardAllowedRate = maxRewardAllowedRate;
    }
}
