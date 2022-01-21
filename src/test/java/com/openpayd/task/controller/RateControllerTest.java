package com.openpayd.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.task.feignclient.ExchangeRateApi;
import com.openpayd.task.feignclient.model.ExchangeCodeList;
import com.openpayd.task.feignclient.model.ExchangeRate;
import com.openpayd.task.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = RateController.class)
public class RateControllerTest {

    @MockBean
    private ExchangeRateApi exchangeRateApi;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void shouldGetCodes() throws Exception{
        Mockito.when(exchangeRateApi.getCodes()).thenReturn(getTestExchangeCodeList());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/rates/codes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(getTestExchangeCodeList().getSupportedCodes())));
    }

    @Test
    public void shouldGetExchangeRate() throws Exception{
        Mockito.when(exchangeRateApi.getExchangeRate("USD","TRY")).thenReturn(getTestExchangeRate());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/rates/from/USD/to/TRY")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(getTestExchangeRate())));
    }

    private ExchangeCodeList getTestExchangeCodeList(){
        ExchangeCodeList exchangeCodeList = new ExchangeCodeList();
        ArrayList<ArrayList<String>> supportedCodes = new ArrayList<>();
        supportedCodes.add(new ArrayList(List.of("USD", "DOLLAR")));
        supportedCodes.add(new ArrayList(List.of("EUR","EURO")));
        exchangeCodeList.setSupportedCodes(supportedCodes);
        return exchangeCodeList;
    }

    private ExchangeRate getTestExchangeRate(){
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCode("USD");
        exchangeRate.setTargetCode("TRY");
        exchangeRate.setConversionRate(5.2);
        return exchangeRate;
    }
}
