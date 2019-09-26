package ru.desiolab.money.transfer;

import ru.desiolab.money.transfer.config.TestModule;

public class TestApp extends App {

    public TestApp() {
        super(new Config()
                .enableWebServer(false)
                .module(new TestModule()));
        this.init();
    }
}
