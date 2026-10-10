package academy.cashbackmerge;

import academy.cashbackmerge.cli.BoardPrinter;
import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.model.GameState;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Path EXAMPLE_CONFIG = Path.of("config", "campaign.example.json");
    private static final long DEFAULT_SEED = 42L;

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));

        CampaignConfig config = ConfigLoader.load(EXAMPLE_CONFIG);
        GameState state = GameState.createInitial(config, DEFAULT_SEED);
        System.out.println(BoardPrinter.print(state.board()));
    }
}
