package com.openpayd.task.repository;

import com.openpayd.task.entity.TransactionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp(){
        transactionRepository.deleteAll();
    }

    @Test
    public void shouldFindTransactionByTransactionId(){
        TransactionEntity transactionEntity = getTestTransactionEntity(null);
        transactionEntity = transactionRepository.save(transactionEntity);

        Optional<TransactionEntity> savedTransactionEntity = transactionRepository.findById(transactionEntity.getId());
        Assertions.assertTrue(savedTransactionEntity.isPresent());

        Optional<TransactionEntity> foundByTID = transactionRepository.findByTransactionId(savedTransactionEntity.get().getTransactionId());
        Assertions.assertTrue(foundByTID.isPresent());
        Assertions.assertEquals(savedTransactionEntity.get().getTransactionId(), foundByTID.get().getTransactionId());
    }

    @Test
    public void shouldFindTransactionsByDate(){
        TransactionEntity transactionEntity = getTestTransactionEntity(1L);
        transactionRepository.save(transactionEntity);
        TransactionEntity transactionEntity1 = getTestTransactionEntity(2L);
        transactionRepository.save(transactionEntity1);

        Date startDate = new Date(System.currentTimeMillis() - 150000L);
        Date endDate = new Date(System.currentTimeMillis() + 150000L);

        Page<TransactionEntity> page = transactionRepository.findAllByTransactionDateBetween(startDate,endDate, PageRequest.of(0,2));

        Assertions.assertTrue(page.hasContent());
        Assertions.assertEquals(2,page.getTotalElements());
        Assertions.assertNotNull(page.getContent().get(0).getTransactionId());
        Assertions.assertNotNull(page.getContent().get(1).getTransactionId());
    }

    private TransactionEntity getTestTransactionEntity(Long id){
        Date date = new Date();
        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setId(id);
        transactionEntity1.setSourceCurrency("USD");
        transactionEntity1.setTargetCurrency("TRY");
        transactionEntity1.setRate(BigDecimal.valueOf(5.2));
        transactionEntity1.setSourceAmount(BigDecimal.TEN);
        transactionEntity1.setTargetAmount(BigDecimal.TEN.multiply(BigDecimal.valueOf(5.2)));
        transactionEntity1.setCreatedAt(date);
        return transactionEntity1;
    }
}
