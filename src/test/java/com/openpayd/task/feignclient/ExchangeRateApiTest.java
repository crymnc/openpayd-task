package com.openpayd.task.feignclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.openpayd.task.config.ExchangeRateMocks;
import com.openpayd.task.config.WireMockConfig;
import com.openpayd.task.feignclient.model.ExchangeCodeList;
import com.openpayd.task.feignclient.model.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@SpringBootTest
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
public class ExchangeRateApiTest {

    @Autowired
    private WireMockServer mockExchangeRateService;

    @Autowired
    private ExchangeRateApi exchangeRateApi;

    @BeforeEach
    void setUp() throws IOException {
        ExchangeRateMocks.setupMockExchangeRateResponse(mockExchangeRateService);
    }

    @Test
    public void shouldGetCodes(){
        ExchangeCodeList exchangeCodeList = exchangeRateApi.getCodes();
        Assertions.assertNotNull(exchangeCodeList);
        Assertions.assertNotNull(exchangeCodeList.getSupportedCodes());
    }

    @Test
    public void shouldGetExchangeRate(){
        ExchangeRate exchangeRate = exchangeRateApi.getExchangeRate("USD","TRY");
        Assertions.assertNotNull(exchangeRate);
        Assertions.assertNotNull(exchangeRate.getConversionRate());
        Assertions.assertEquals("USD",exchangeRate.getBaseCode());
        Assertions.assertEquals("TRY", exchangeRate.getTargetCode());
    }


}
