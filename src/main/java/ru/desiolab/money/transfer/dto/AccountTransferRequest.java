package ru.desiolab.money.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(fluent = true)
public class AccountTransferRequest {
    @JsonProperty
    private Integer fromAccountId;
    @JsonProperty
    private Integer toAccountId;
    @JsonProperty
    private BigDecimal amount;
}
