package ru.desiolab.money.transfer;

import ru.desiolab.money.transfer.config.MainModule;

import static io.jooby.Jooby.runApp;

public class MainClass {
    public static void main(String[] args) {
        runApp(args, () -> {
            App.Config config = new App.Config()
                    .enableWebServer(true)
                    .module(new MainModule());
            App app = new App(config);
            app.init();
            return app;
        });
    }
}
