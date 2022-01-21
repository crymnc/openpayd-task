package com.openpayd.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.task.domain.PageResponse;
import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.mapper.TransactionMapper;
import com.openpayd.task.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

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
    public void shouldReturnTransactionById() throws Exception{
        TransactionEntity transactionEntity = getTestTransactionEntity();
        Transaction transaction = getTestTransaction(transactionEntity);
        Mockito.when(transactionService.findByTransactionId(any())).thenReturn(transactionEntity);
        Mockito.when(transactionMapper.toModel(any())).thenReturn(transaction);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/transactions/12312ee3ff")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(transaction)));
    }

    @Test
    public void shouldReturnTransactionsBetweenDates() throws Exception{
        Page<TransactionEntity> page = getTestTransactionPage();
        List<Transaction> transactions = getTestTransactionList(getTestTransactionPage().getContent());
        Mockito.when(transactionService.findAllByTransactionDateBetweenWithPage(any(),any(),any())).thenReturn(page);
        Mockito.when(transactionMapper.toModelList(any())).thenReturn(transactions);
        PageResponse pageResponse = PageResponse.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(transactions).build();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/transactions/between/2022-01-20/and/2022-01-21")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(pageResponse)));
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
    public List<Transaction> getTestTransactionList(List<TransactionEntity> transactionEntities){
        if(CollectionUtils.isEmpty(transactionEntities))
            return new ArrayList<>();
        return transactionEntities.stream().map(transactionEntity -> getTestTransaction(transactionEntity)).collect(Collectors.toList());
    }

    private Page<TransactionEntity> getTestTransactionPage(){
        TransactionEntity transactionEntity1 = getTestTransactionEntity();
        TransactionEntity transactionEntity2 = getTestTransactionEntity();
        PageImpl<TransactionEntity> page = new PageImpl(List.of(transactionEntity1,transactionEntity2));
        return page;
    }
}
