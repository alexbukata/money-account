package ru.desiolab.money.transfer.repository;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.desiolab.money.transfer.dto.AccountFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountDaoTransactionTest {

    private AccountDao accountDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        JdbcConnectionPool jdbcConnectionPool = Mockito.mock(JdbcConnectionPool.class);
        connection = Mockito.mock(Connection.class);
        when(jdbcConnectionPool.getConnection()).thenReturn(connection);
        accountDao = new AccountDao(jdbcConnectionPool, new AccountFactory());
    }

    @Test
    void transactionCommit() throws Exception {
        //act&assert
        accountDao.doInTransaction(connection -> {
        });
        verify(connection).setAutoCommit(eq(false));
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection).commit();
        verify(connection, times(0)).rollback();
    }

    @Test
    void transactionRollback() throws Exception {
        //act&assert
        assertThrows(RuntimeException.class, () -> accountDao.doInTransaction(connection -> {
            throw new RuntimeException();
        }));
        verify(connection).setAutoCommit(eq(false));
        verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        verify(connection, times(0)).commit();
        verify(connection).rollback();
    }
}
