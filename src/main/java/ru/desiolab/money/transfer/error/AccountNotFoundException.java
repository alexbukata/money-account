package ru.desiolab.money.transfer.error;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String format) {
        super(format);
    }
}
