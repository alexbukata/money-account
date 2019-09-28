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
            return accountTransferController.transferMoney(request);
        } catch (JsonProcessingException e) {
            log.error("transferMoney.err", e);
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            return new Response<String>()
                    .success(false)
                    .errorMessage("Allowed fields in request: 'fromAccountId', 'toAccountId', 'amount'");
        } catch (Exception e) {
            log.error("transferMoney.err", e);
            ctx.setResponseCode(StatusCode.SERVER_ERROR_CODE);
            return new Response<String>()
                    .success(false)
                    .errorMessage("Internal error occurred");
        }
    }
}
