package com.openpayd.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.feignclient.ExchangeRateApi;
import com.openpayd.task.feignclient.model.ExchangeRate;
import com.openpayd.task.mapper.TransactionMapper;
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

import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = ConversionController.class)
public class ConversionControllerTest {

    @MockBean
    private ExchangeRateApi exchangeRateApi;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionMapper transactionMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

    }

    @Test
    public void shouldConvertGivenCurrencyAndCreateATransaction() throws Exception{
        TransactionEntity transactionEntity = getTestTransactionEntity();
        Mockito.when(exchangeRateApi.getExchangeRate("USD","TRY")).thenReturn(getTestExchangeRate());
        Mockito.when(transactionService.createTransaction("USD","TRY", BigDecimal.TEN,BigDecimal.valueOf(5.2))).thenReturn(transactionEntity);
        Mockito.when(transactionMapper.toModel(any())).thenReturn(getTestTransaction(transactionEntity));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/convert/from/USD/to/TRY/amount/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(getTestTransaction(transactionEntity))));
    }

    private ExchangeRate getTestExchangeRate(){
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCode("USD");
        exchangeRate.setTargetCode("TRY");
        exchangeRate.setConversionRate(5.2);
        return exchangeRate;
    }

    private TransactionEntity getTestTransactionEntity(){
        Date date = new Date();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(1L);
        transactionEntity.setSourceCurrency("USD");
        transactionEntity.setTargetCurrency("TRY");
        transactionEntity.setRate(BigDecimal.valueOf(5.2));
        transactionEntity.setSourceAmount(BigDecimal.TEN);
        transactionEntity.setTargetAmount(BigDecimal.TEN.multiply(BigDecimal.valueOf(5.2)));
        transactionEntity.setCreatedAt(date);
        return transactionEntity;
    }

    private Transaction getTestTransaction(TransactionEntity transactionEntity){
        if(transactionEntity == null)
            return null;
        Transaction transaction = Transaction.builder()
                .transactionId(transactionEntity.getTransactionId())
                .transactionDate(transactionEntity.getTransactionDate())
                .sourceCurrency(transactionEntity.getSourceCurrency())
                .targetCurrency(transactionEntity.getTargetCurrency())
                .sourceAmount(transactionEntity.getSourceAmount())
                .targetAmount(transactionEntity.getTargetAmount())
                .rate(transactionEntity.getRate()).build();
        return transaction;
    }
}
