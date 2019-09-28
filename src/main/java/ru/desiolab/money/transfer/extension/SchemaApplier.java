package ru.desiolab.money.transfer.extension;

import io.jooby.Jooby;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaApplier {

    private final JdbcConnectionPool jdbcConnectionPool;

    @Inject
    public SchemaApplier(JdbcConnectionPool jdbcConnectionPool) {
        this.jdbcConnectionPool = jdbcConnectionPool;
    }

    public void apply() throws IOException, SQLException {
        InputStream schemaSqlStream = getClass().getClassLoader().getResourceAsStream("sql/schema.sql");
        String schemaSql = new String(schemaSqlStream.readAllBytes(), StandardCharsets.UTF_8);
        Connection connection = jdbcConnectionPool.getConnection();
        connection.createStatement().execute(schemaSql);
    }

    public static class Extension implements io.jooby.Extension {
        @Override
        public void install(@Nonnull Jooby application) {
            application.onStarted(() -> {
                SchemaApplier schemaApplier = application.require(SchemaApplier.class);
                schemaApplier.apply();
            });
        }
    }
}
