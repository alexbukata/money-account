package ru.desiolab.money.transfer;

import com.fasterxml.jackson.core.JsonParseException;
import io.jooby.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.desiolab.money.transfer.controller.AccountTransferController;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AppTest {

    private MockRouter mockRouter;
    private AccountTransferController accountTransferController;
    private Context context;

    @BeforeEach
    void setUp() {
        App app = new App(new App.Config());
        app.initHandlers();
        mockRouter = new MockRouter(app);
        accountTransferController = mock(AccountTransferController.class);
        context = Mockito.mock(Context.class);
    }

    @Test
    void transferMoneySuccess() throws Exception {
        //arrange
        AccountTransferRequest request = new AccountTransferRequest();
        when(context.body(eq(AccountTransferRequest.class), eq(MediaType.json))).thenReturn(request);
        when(context.require(AccountTransferController.class)).thenReturn(accountTransferController);
        //act
        Response response = mockRouter.post("/account/transfer", context).value(Response.class);
        //assert
        assertTrue(response.success());
        verify(accountTransferController).transferMoney(eq(request));
    }

    @Test
    void transferMoneyBadRequest() {
        //arrange
        when(context.body(eq(AccountTransferRequest.class), eq(MediaType.json)))
                .then(invocation -> SneakyThrows.propagate(new JsonParseException(null, "err")));
        when(context.require(AccountTransferController.class)).thenReturn(accountTransferController);
        //act
        Response response = mockRouter.post("/account/transfer", context).value(Response.class);
        //assert
        assertFalse(response.success());
        verify(context).setResponseCode(eq(StatusCode.BAD_REQUEST));
        verifyZeroInteractions(accountTransferController);
    }

    @Test
    void transferMoneyServerError() throws Exception {
        //arrange
        AccountTransferRequest request = new AccountTransferRequest();
        when(context.body(eq(AccountTransferRequest.class), eq(MediaType.json))).thenReturn(request);
        when(context.require(AccountTransferController.class)).thenReturn(accountTransferController);
        doThrow(RuntimeException.class).when(accountTransferController).transferMoney(eq(request));
        //act
        Response response = mockRouter.post("/account/transfer", context).value(Response.class);
        //assert
        assertFalse(response.success());
        verify(context).setResponseCode(eq(StatusCode.SERVER_ERROR));
    }
}