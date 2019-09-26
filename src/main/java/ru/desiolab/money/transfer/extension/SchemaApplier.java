package ru.desiolab.money.transfer.extension;

import io.jooby.Jooby;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaApplier {

    private final JdbcConnectionPool jdbcConnectionPool;

    @Inject
    public SchemaApplier(JdbcConnectionPool jdbcConnectionPool) {
        this.jdbcConnectionPool = jdbcConnectionPool;
    }

    private void apply() throws URISyntaxException, IOException, SQLException {
        URL schemaSqlStream = getClass().getClassLoader().getResource("sql/schema.sql");
        String schemaSql = Files.readString(Path.of(schemaSqlStream.toURI()));
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
