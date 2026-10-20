package academy.cashbackmerge.cli;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.GameState;
import academy.cashbackmerge.model.MoveResult;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "play", mixinStandardHelpOptions = true, description = "Консольная игра")
public class PlayCommand implements Callable<Integer> {

    private static final long GAME_SEED = 42L;
    private static final String DEFAULT_CONFIG = "config/campaign.example.json";

    @Option(names = "--config", defaultValue = DEFAULT_CONFIG, description = "Путь к конфигу")
    private Path configPath = Path.of(DEFAULT_CONFIG);

    @Override
    public Integer call() throws Exception {
        CampaignConfig config = ConfigLoader.load(configPath);
        GameState state = GameState.createInitial(config, GAME_SEED);

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Добро пожаловать в Cashback Merge!");
        System.out.println("Управление: w (вверх), s (вниз), a (влево), d (вправо). q (выход).");

        while (!state.isGameOver()) {
            System.out.println("\n" + BoardPrinter.print(state.getBoard()));
            System.out.println("Осталось ходов: " + (state.getMovesLimit() - state.getMovesUsed()));
            System.out.print("Ваш ход: ");

            String input = scanner.nextLine().trim().toLowerCase();
            Direction dir =
                    switch (input) {
                        case "w" -> Direction.UP;
                        case "s" -> Direction.DOWN;
                        case "a" -> Direction.LEFT;
                        case "d" -> Direction.RIGHT;
                        case "q" -> null;
                        default -> {
                            System.out.println("Неверный ввод!");
                            yield null;
                        }
                    };

            if (dir == null) {
                if ("q".equals(input)) {
                    break;
                }
                continue;
            }

            MoveResult result = state.applyMove(dir);
            if (!result.changed()) {
                System.out.println("Ход не изменил поле.");
            }
        }

        System.out.println("\nИгра окончена!");
        System.out.println(BoardPrinter.print(state.getBoard()));
        System.out.println("Ваш итоговый Score: " + state.getScore());
        return 0;
    }
}
