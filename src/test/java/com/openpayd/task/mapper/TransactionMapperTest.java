package com.openpayd.task.mapper;

import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionMapperTest {

    TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    public void shouldConvertEntityToModel(){
        TransactionEntity transactionEntity = getTestTransactionEntity();
        Transaction transaction = transactionMapper.toModel(transactionEntity);
        assertThat(transaction).isInstanceOf(Transaction.class).usingRecursiveComparison().isEqualTo(transactionEntity);
    }

    @Test
    public void shouldReturnNullIfEntityIsNull(){
        TransactionEntity transactionEntity = null;
        Transaction transaction = transactionMapper.toModel(null);
        Assertions.assertNull(transaction);
    }

    @Test
    public void shouldConvertEntityListToModelList(){
        TransactionEntity transactionEntity1 = getTestTransactionEntity();
        TransactionEntity transactionEntity2 = getTestTransactionEntity();
        List<Transaction> transactions = transactionMapper.toModelList(List.of(transactionEntity1,transactionEntity2));
        assertThat(transactions).isInstanceOf(List.class).usingRecursiveComparison().isEqualTo(List.of(transactionEntity1,transactionEntity2));
    }

    @Test
    public void shouldReturnEmptyListIfEntityListIsEmpty(){
        List<Transaction> transactions = transactionMapper.toModelList(new ArrayList<>());
        Assertions.assertEquals(new ArrayList(), transactions);
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
}
