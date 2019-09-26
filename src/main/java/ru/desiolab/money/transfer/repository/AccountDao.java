package ru.desiolab.money.transfer.repository;

import org.h2.jdbcx.JdbcConnectionPool;
import ru.desiolab.money.transfer.dto.Account;
import ru.desiolab.money.transfer.dto.AccountFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

public class AccountDao {

    private final JdbcConnectionPool jdbcConnectionPool;
    private final AccountFactory accountFactory;

    @Inject
    private AccountDao(JdbcConnectionPool jdbcConnectionPool, AccountFactory accountFactory) {
        this.jdbcConnectionPool = jdbcConnectionPool;
        this.accountFactory = accountFactory;
    }

    public Connection getConnection() throws SQLException {
        return jdbcConnectionPool.getConnection();
    }

    public Account getAccount(Connection connection, Integer accountId) throws SQLException {
        String query = "SELECT id, customerName, amount FROM ACCOUNT WHERE id=? FOR UPDATE";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accountId);
        boolean haveResult = preparedStatement.execute();
        RuntimeException notFoundException = new RuntimeException(format("Can't find account with id=%d", accountId));
        if (!haveResult) {
            throw notFoundException;
        }
        try (ResultSet rs = preparedStatement.getResultSet()) {
            return accountFactory.accountFrom(rs)
                    .orElseThrow(() -> notFoundException);
        }
    }

    public void updateAccountAmount(Connection connection, Integer accountId, BigDecimal amount) throws SQLException {
        String updateQuery = "UPDATE ACCOUNT SET amount=? where id=?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setBigDecimal(1, amount);
        updateStatement.setInt(2, accountId);
        updateStatement.executeUpdate();
    }
}
