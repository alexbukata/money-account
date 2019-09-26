package ru.desiolab.money.transfer.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import io.jooby.JoobyTest;
import io.jooby.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.desiolab.money.transfer.TestApp;
import ru.desiolab.money.transfer.dto.AccountTransferRequest;
import ru.desiolab.money.transfer.dto.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JoobyTest(value = TestApp.class, port = 8911)
class AccountTransferControllerTest {

    private HttpClient http;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        http = HttpClient.newBuilder().build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void transferMoneyHappyPath() throws IOException, URISyntaxException, InterruptedException {
        AccountTransferRequest requestDto = new AccountTransferRequest()
                .fromAccountId(1)
                .toAccountId(2)
                .amount(BigDecimal.valueOf(100L));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8911/account/transfer"))
                .POST(ofString(objectMapper.writeValueAsString(requestDto), StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.JSON)
                .header(HttpHeaders.ACCEPT, MediaType.JSON)
                .build();
        String responseRaw = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        Response<String> response = objectMapper.readValue(responseRaw, new TypeReference<Response<String>>() {
        });
        assertTrue(response.success());
    }
}