package ru.desiolab.money.transfer.config;

import com.google.inject.AbstractModule;
import org.h2.jdbcx.JdbcConnectionPool;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JdbcConnectionPool.class).toInstance(JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", ""));
    }
}
