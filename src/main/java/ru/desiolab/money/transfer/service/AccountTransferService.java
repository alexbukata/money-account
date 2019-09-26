package ru.desiolab.money.transfer.service;

import ru.desiolab.money.transfer.dto.Account;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;
import ru.desiolab.money.transfer.repository.AccountDao;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;

public class AccountTransferService {

    private final AccountDao accountDao;

    @Inject
    private AccountTransferService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Response<String> transferMoney(AccountTransferRequest request) {
        try {
            validate(request);
            doTransferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
            return new Response<String>()
                    .success(true);
        } catch (Exception e) {
            //TODO return user-friendly message, log internal message
            return new Response<String>()
                    .success(false)
                    .errorMessage(e.getMessage());
        }
    }

    private void validate(AccountTransferRequest request) {
        if (request.fromAccountId() == null || request.toAccountId() == null || request.amount() == null) {
            throw new RuntimeException("fromAccountId, toAccountId, amount is required parameters");
        }
        if (request.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("You can transfer only positive amount of money");
        }
    }

    private void doTransferMoney(Integer fromAccountId, Integer toAccountId, BigDecimal amount) throws Exception {
        Connection connection = accountDao.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        try {
            Account fromAccount = accountDao.getAccount(connection, fromAccountId);
            if (fromAccount.amount().compareTo(amount) < 0) {
                throw new RuntimeException("Not enough money on source account!");
            }
            Account toAccount = accountDao.getAccount(connection, toAccountId);
            BigDecimal newFromAccountAmount = fromAccount.amount().subtract(amount);
            BigDecimal newToAccountAmount = toAccount.amount().add(amount);
            accountDao.updateAccountAmount(connection, fromAccount.id(), newFromAccountAmount);
            accountDao.updateAccountAmount(connection, toAccount.id(), newToAccountAmount);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}
