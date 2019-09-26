package ru.desiolab.money.transfer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(fluent = true)
public class Account {
    private Integer id;
    private String customerName;
    private BigDecimal amount;
}
