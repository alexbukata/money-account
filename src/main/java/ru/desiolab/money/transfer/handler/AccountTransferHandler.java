package ru.desiolab.money.transfer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jooby.Context;
import io.jooby.MediaType;
import io.jooby.Route;
import io.jooby.StatusCode;
import lombok.extern.slf4j.Slf4j;
import ru.desiolab.money.transfer.controller.AccountTransferController;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;

import javax.annotation.Nonnull;

@Slf4j
public class AccountTransferHandler implements Route.Handler {

    private final AccountTransferController accountTransferController;

    public AccountTransferHandler(AccountTransferController accountTransferController) {
        this.accountTransferController = accountTransferController;
    }

    @Nonnull
    @Override
    public Object apply(@Nonnull Context ctx) {
        try {
            AccountTransferRequest request = ctx.body(AccountTransferRequest.class, MediaType.json);
            accountTransferController.transferMoney(request);
            return new Response().success(true);
        } catch (JsonProcessingException e) {
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            return new Response()
                    .success(false)
                    .errorMessage("Allowed fields in request: 'fromAccountId', 'toAccountId', 'amount'");
        } catch (Exception e) {
            ctx.setResponseCode(StatusCode.SERVER_ERROR);
            return new Response()
                    .success(false)
                    .errorMessage("Internal error occurred");
        }
    }
}
