package ru.desiolab.money.transfer;

import com.google.inject.Module;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.di.GuiceModule;
import io.jooby.json.JacksonModule;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.h2.tools.Server;
import ru.desiolab.money.transfer.controller.AccountTransferController;
import ru.desiolab.money.transfer.extension.SchemaApplier;

public class App extends Jooby {

    private final Config config;

    public App(Config config) {
        this.config = config;
    }

    public void init() {
        initModules();
        runH2WebServer();
        initHandlers();
    }

    private void initModules() {
        install(new GuiceModule(config.module));
        install(new JacksonModule());
        install(new SchemaApplier.Extension());
    }

    private void initHandlers() {
        post("/account/transfer",
                ctx -> require(AccountTransferController.class).transferMoney(ctx)
        ).produces(MediaType.json).consumes(MediaType.json);
    }

    @SneakyThrows
    private void runH2WebServer() {
        if (config.enableWebServer) {
            Server.createWebServer("-webPort", "8081").start();
        }
    }

    @Setter
    @Accessors(fluent = true)
    public static class Config {
        private boolean enableWebServer = false;
        private Module module;
    }
}
