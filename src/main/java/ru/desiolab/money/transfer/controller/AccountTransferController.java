package ru.desiolab.money.transfer.controller;

import io.jooby.Context;
import io.jooby.MediaType;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;
import ru.desiolab.money.transfer.service.AccountTransferService;

import javax.inject.Inject;

public class AccountTransferController {

    private final AccountTransferService accountTransferService;

    @Inject
    private AccountTransferController(AccountTransferService accountTransferService) {
        this.accountTransferService = accountTransferService;
    }

    public Response<String> transferMoney(Context ctx) {
        AccountTransferRequest request = ctx.body(AccountTransferRequest.class, MediaType.json);
        return accountTransferService.transferMoney(request);
    }
}
