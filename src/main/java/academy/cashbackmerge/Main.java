package academy.cashbackmerge;

import academy.cashbackmerge.cli.SimulateCommand;
import picocli.CommandLine;

@CommandLine.Command(
        name = "cashback-merge",
        subcommands = {SimulateCommand.class})
public class Main implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        // Если запущено без подкоманд, выводим справку
        CommandLine.usage(this, System.out);
    }
}
