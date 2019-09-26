package ru.desiolab.money.transfer.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountFactory {

    public Optional<Account> accountFrom(ResultSet resultSet) throws SQLException {
        if (!resultSet.first()) {
            return Optional.empty();
        }
        Account account = new Account()
                .id(resultSet.getInt(1))
                .customerName(resultSet.getString(2))
                .amount(resultSet.getBigDecimal(3));
        return Optional.of(account);
    }
}
