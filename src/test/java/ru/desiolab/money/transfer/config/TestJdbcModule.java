package ru.desiolab.money.transfer.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.h2.jdbcx.JdbcConnectionPool;

public class TestJdbcModule extends AbstractModule {

    @Provides
    private JdbcConnectionPool jdbcConnectionPool() {
        return JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    }
}
