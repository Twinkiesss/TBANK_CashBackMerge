package academy.cashbackmerge.cli;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.simulation.ReplayRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "replay", mixinStandardHelpOptions = true, description = "Запись и воспроизведение партии")
public class ReplayCommand implements Callable<Integer> {

    private static final String DEFAULT_CONFIG = "config/campaign.example.json";

    @Option(names = "--config", defaultValue = DEFAULT_CONFIG)
    private Path configPath = Path.of(DEFAULT_CONFIG);

    @Option(names = "--seed", defaultValue = "42")
    private long seed = 42;

    @Option(names = "--output", defaultValue = "replay.json")
    private Path output = Path.of("replay.json");

    @Option(names = "--play", description = "Воспроизвести партию из файла")
    private boolean playMode = false;

    @Override
    public Integer call() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CampaignConfig config = ConfigLoader.load(configPath);
        GameState state = GameState.createInitial(config, seed);

        if (playMode) {
            ReplayRecord record = mapper.readValue(output.toFile(), ReplayRecord.class);
            state = GameState.createInitial(config, record.seed());
            System.out.println("Воспроизведение партии с seed " + record.seed());

            for (Direction dir : record.moves()) {
                state.applyMove(dir);
            }
            System.out.println(BoardPrinter.print(state.getBoard()));
            System.out.println("Score: " + state.getScore());
        } else {
            // Запись (играем случайно и сохраняем)
            List<Direction> moves = new ArrayList<>();
            Direction[] dirs = Direction.values();
            java.util.Random rng = new java.util.Random(seed);

            while (!state.isGameOver()) {
                Direction dir = dirs[rng.nextInt(dirs.length)];
                if (state.applyMove(dir).changed()) {
                    moves.add(dir);
                }
            }

            mapper.writeValue(output.toFile(), new ReplayRecord(seed, moves));
            System.out.println("Партия записана в " + output.toAbsolutePath());
            System.out.println("Score: " + state.getScore());
        }
        return 0;
    }
}
