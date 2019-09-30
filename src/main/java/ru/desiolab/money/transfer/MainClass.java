package ru.desiolab.money.transfer;

import org.apache.commons.cli.*;
import ru.desiolab.money.transfer.config.MainModule;

import static io.jooby.Jooby.runApp;

public class MainClass {
    public static void main(String[] args) throws ParseException {
        int workerNumber = parseWorkerNumber(args);
        runApp(args, () -> {
            App.Config config = new App.Config()
                    .enableWebServer(true)
                    .workerNumber(workerNumber)
                    .module(new MainModule(workerNumber));
            App app = new App(config);
            app.init();
            return app;
        });
    }

    private static int parseWorkerNumber(String[] args) throws ParseException {
        Options options = new Options();
        Option iexOption = Option.builder()
                .argName("Number of workers")
                .longOpt("workerThreads")
                .required(false)
                .hasArg()
                .build();
        options.addOption(iexOption);
        CommandLine parser = new DefaultParser().parse(options, args);
        return Integer.parseInt(parser.getOptionValue("workerThreads", "64"));
    }
}
