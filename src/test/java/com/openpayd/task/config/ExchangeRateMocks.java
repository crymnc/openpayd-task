package com.openpayd.task.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;


public class ExchangeRateMocks {

    static String ACCESS_KEY = "/2df898732a5ea9a62bd379a3";

    public static void setupMockExchangeRateResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo(ACCESS_KEY+"/codes"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        ExchangeRateMocks.class.getClassLoader().getResourceAsStream("payload/get-codes-response.json"),
                                        defaultCharset()))));

        mockService.stubFor(WireMock.get(WireMock.urlEqualTo(ACCESS_KEY+"/pair/USD/TRY"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        ExchangeRateMocks.class.getClassLoader().getResourceAsStream("payload/get-exchange-rate-response.json"),
                                        defaultCharset()))));
    }
}
