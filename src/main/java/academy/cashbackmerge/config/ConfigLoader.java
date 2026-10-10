package academy.cashbackmerge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ConfigLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final BigDecimal ONE = BigDecimal.ONE;

    private ConfigLoader() {}

    public static CampaignConfig load(Path path) {
        try {
            String json = Files.readString(path);
            CampaignConfig config = MAPPER.readValue(json, CampaignConfig.class);
            validate(config);
            return config;
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать конфиг: " + path, e);
        }
    }

    public static void validate(CampaignConfig config) {
        if (config.movesLimit() <= 0) {
            throw new IllegalArgumentException("movesLimit должен быть > 0");
        }
        if (config.boardSize() <= 0) {
            throw new IllegalArgumentException("boardSize должен быть > 0");
        }

        List<SpawnRule> rules = config.spawnRules();
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("spawnRules не должен быть пустым");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (SpawnRule rule : rules) {
            if (rule.probability().signum() <= 0) {
                throw new IllegalArgumentException("Вероятность должна быть > 0: " + rule.probability());
            }
            sum = sum.add(rule.probability());
        }
        if (sum.compareTo(ONE) != 0) {
            throw new IllegalArgumentException("Сумма вероятностей должна быть 1, а не " + sum);
        }
    }
}
