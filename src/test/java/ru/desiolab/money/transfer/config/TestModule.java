package ru.desiolab.money.transfer.config;

import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new TestJdbcModule());
    }
}
