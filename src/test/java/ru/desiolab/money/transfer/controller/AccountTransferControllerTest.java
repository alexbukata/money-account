package ru.desiolab.money.transfer.controller;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.desiolab.money.transfer.dto.Account;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;
import ru.desiolab.money.transfer.repository.AccountDao;
import ru.desiolab.money.transfer.repository.Consumer;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AccountTransferControllerTest {

    @Bind
    private AccountDao accountDao = Mockito.mock(AccountDao.class);

    @Inject
    private AccountTransferController accountTransferController;

    @BeforeEach
    void setUp() throws Exception {
        doAnswer(invocationOnMock -> {
            Consumer consumer = invocationOnMock.getArgument(0, Consumer.class);
            consumer.accept(Mockito.mock(Connection.class));
            return null;
        }).when(accountDao).doInTransaction(any());
        Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
    }

    @Test
    void transferMoneySuccess() throws Exception {
        //arrange
        when(accountDao.getAccount(any(), eq(1))).thenReturn(firstAccount());
        when(accountDao.getAccount(any(), eq(2))).thenReturn(secondAccount());
        AccountTransferRequest requestDto = new AccountTransferRequest()
                .fromAccountId(1)
                .toAccountId(2)
                .amount(BigDecimal.valueOf(100L));
        //act
        Response<String> response = accountTransferController.transferMoney(requestDto);
        //assert
        assertTrue(response.success());
        verify(accountDao).updateAccountAmount(any(), eq(1), eq(BigDecimal.valueOf(900)));
        verify(accountDao).updateAccountAmount(any(), eq(2), eq(BigDecimal.valueOf(110)));
    }

    @Test
    void sourceAccountNotEnoughMoney() throws Exception {
        //arrange
        when(accountDao.getAccount(any(), eq(1))).thenReturn(firstAccount());
        when(accountDao.getAccount(any(), eq(2))).thenReturn(secondAccount());
        AccountTransferRequest requestDto = new AccountTransferRequest()
                .fromAccountId(1)
                .toAccountId(2)
                .amount(BigDecimal.valueOf(10000L));
        //act
        assertThrows(RuntimeException.class, () -> accountTransferController.transferMoney(requestDto));
        //assert
        verify(accountDao, times(0)).updateAccountAmount(any(), any(), any());
    }

    @Test
    void sourceAccountNegativeAmount() {
        //arrange
        AccountTransferRequest requestDto = new AccountTransferRequest()
                .fromAccountId(1)
                .toAccountId(2)
                .amount(BigDecimal.valueOf(-100L));
        //act
        assertThrows(RuntimeException.class, () -> accountTransferController.transferMoney(requestDto));
        //assert
        verifyZeroInteractions(accountDao);
    }

    private Account firstAccount() {
        return new Account()
                .id(1)
                .customerName("name1")
                .amount(BigDecimal.valueOf(1000));
    }

    private Account secondAccount() {
        return new Account()
                .id(2)
                .customerName("name2")
                .amount(BigDecimal.valueOf(10));
    }
}