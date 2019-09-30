package ru.desiolab.money.transfer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    @JsonProperty
    private Boolean success;
    @JsonProperty
    private String errorMessage;
}
