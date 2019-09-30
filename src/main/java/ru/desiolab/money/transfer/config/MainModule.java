package ru.desiolab.money.transfer.config;

import com.google.inject.AbstractModule;
import org.h2.jdbcx.JdbcConnectionPool;

public class MainModule extends AbstractModule {

    private final int jdbcMaxConnections;

    public MainModule(int jdbcMaxConnections) {
        this.jdbcMaxConnections = jdbcMaxConnections;
    }

    @Override
    protected void configure() {
        JdbcConnectionPool connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
        connectionPool.setMaxConnections(jdbcMaxConnections);
        bind(JdbcConnectionPool.class).toInstance(connectionPool);
    }
}
