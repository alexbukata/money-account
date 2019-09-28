package ru.desiolab.money.transfer.repository;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t) throws Exception;
}
