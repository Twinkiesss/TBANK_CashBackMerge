package academy.cashbackmerge.cli;

import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.simulation.CompareSimulator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "compare", mixinStandardHelpOptions = true, description = "Сравнение двух кампаний")
public class CompareCommand implements Callable<Integer> {

    @Option(names = "--config1", required = true)
    private Path config1Path = Path.of("config/campaign.example.json");

    @Option(names = "--config2", required = true)
    private Path config2Path = Path.of("config/campaign.example.json");

    @Option(names = "--players", defaultValue = "1000")
    private int players = 1000;

    @Option(names = "--seed", defaultValue = "42")
    private long seed = 42;

    @Option(names = "--output", defaultValue = "compare.md")
    private Path output = Path.of("compare.md");

    @Override
    public Integer call() throws Exception {
        var c1 = ConfigLoader.load(config1Path);
        var c2 = ConfigLoader.load(config2Path);

        String report = CompareSimulator.compare(c1, c2, players, seed);
        Files.writeString(output, report);

        System.out.println("Сравнение сохранено в " + output.toAbsolutePath());
        return 0;
    }
}
