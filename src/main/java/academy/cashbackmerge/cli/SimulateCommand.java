package academy.cashbackmerge.cli;

import academy.cashbackmerge.config.CampaignConfig;
import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.simulation.RandomStrategy;
import academy.cashbackmerge.simulation.ReportWriter;
import academy.cashbackmerge.simulation.SimulationResult;
import academy.cashbackmerge.simulation.Simulator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "simulate", mixinStandardHelpOptions = true, description = "Запуск batch-симуляции.")
public class SimulateCommand implements Callable<Integer> {

    @Option(names = "--config", required = true, description = "Путь к JSON-конфигу")
    private Path configPath = Path.of("config", "campaign.example.json");

    @Option(names = "--players", defaultValue = "1000", description = "Количество игроков")
    private int players = 1000;

    @Option(names = "--strategy", defaultValue = "random", description = "Стратегия (random)")
    private String strategy = "random";

    @Option(names = "--seed", defaultValue = "42", description = "Seed")
    private long seed = 42;

    @Option(names = "--output", defaultValue = "report.md", description = "Путь к отчёту")
    private Path output = Path.of("report.md");

    @Override
    public Integer call() throws Exception {
        CampaignConfig config = ConfigLoader.load(configPath);

        if (!"random".equals(strategy)) {
            System.err.println("Неизвестная стратегия: " + strategy);
            return 1;
        }

        Simulator simulator = new Simulator(config);
        SimulationResult result = simulator.simulate(players, () -> new RandomStrategy(seed));

        String report = ReportWriter.writeMarkdown(config, players, seed, strategy, result);
        Files.writeString(output, report);

        System.out.println("Симуляция завершена. Отчёт сохранён в " + output.toAbsolutePath());
        return 0;
    }
}
