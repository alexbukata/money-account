package ru.desiolab.money.transfer.service;

import lombok.extern.slf4j.Slf4j;
import ru.desiolab.money.transfer.dto.Account;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;
import ru.desiolab.money.transfer.error.NotEnoughMoneyException;
import ru.desiolab.money.transfer.repository.AccountDao;

import javax.inject.Inject;
import java.math.BigDecimal;

@Slf4j
public class AccountTransferService {

    private final AccountDao accountDao;

    @Inject
    private AccountTransferService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Response<String> transferMoney(AccountTransferRequest request) throws Exception {
        log.info("transferMoney.in request={}", request.toString());
        doTransferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
        log.info("transferMoney.out");
        return new Response<String>().success(true);
    }

    private void doTransferMoney(Integer fromAccountId, Integer toAccountId, BigDecimal amount) throws Exception {
        log.info("doTransferMoney.in fromAccountId={}, toAccountId={}, amount={}", fromAccountId, toAccountId, amount);
        accountDao.doInTransaction(connection -> {
            Account fromAccount = accountDao.getAccount(connection, fromAccountId);
            if (fromAccount.amount().compareTo(amount) < 0) {
                throw new NotEnoughMoneyException("Not enough money on source account!");
            }
            Account toAccount = accountDao.getAccount(connection, toAccountId);
            BigDecimal newFromAccountAmount = fromAccount.amount().subtract(amount);
            BigDecimal newToAccountAmount = toAccount.amount().add(amount);
            accountDao.updateAccountAmount(connection, fromAccount.id(), newFromAccountAmount);
            accountDao.updateAccountAmount(connection, toAccount.id(), newToAccountAmount);
        });
        log.info("doTransferMoney.out");
    }
}
