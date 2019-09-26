package ru.desiolab.money.transfer.config;

import com.google.inject.AbstractModule;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JdbcModule());
    }
}
