package ru.desiolab.money.transfer.repository;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.desiolab.money.transfer.dto.Account;
import ru.desiolab.money.transfer.error.AccountNotFoundException;
import ru.desiolab.money.transfer.extension.SchemaApplier;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountDaoTest {

    @Bind
    private static JdbcConnectionPool connectionPool;

    @Inject
    private AccountDao accountDao;

    @BeforeAll
    static void setUpGlobal() throws IOException, SQLException {
        connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
        new SchemaApplier(connectionPool).apply();
    }

    @BeforeEach
    public void setUp() {
        Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
    }

    @Test
    void getExistedAccount() throws SQLException {
        //act
        Account account = accountDao.getAccount(connectionPool.getConnection(), 1);
        //assert
        assertNotNull(account);
        assertEquals(1, account.id());
        assertEquals("Aleksandr Bukata", account.customerName());
        assertEquals(BigDecimal.valueOf(1000.23), account.amount());
    }

    @Test
    void getUnexistedAccount() {
        //act&assert
        assertThrows(AccountNotFoundException.class, () -> accountDao.getAccount(connectionPool.getConnection(), -1));
    }

    @Test
    void updateAccount() throws SQLException {
        //arrange
        Connection connection = connectionPool.getConnection();
        //act
        accountDao.updateAccountAmount(connection, 1, BigDecimal.ONE);
        //assert
        Account account = accountDao.getAccount(connection, 1);
        assertEquals(BigDecimal.ONE, account.amount());
        assertEquals("Aleksandr Bukata", account.customerName());
    }
}