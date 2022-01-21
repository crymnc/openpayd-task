package com.openpayd.task.service;

import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.exception.BusinessException;
import com.openpayd.task.exception.BusinessExceptions;
import com.openpayd.task.repository.TransactionRepository;
import com.openpayd.task.repository.base.EntityRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EntityRepository entityRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void shouldCreateTransaction(){
        String from = "USD";
        String to = "TRY";
        BigDecimal amount = BigDecimal.valueOf(20);
        BigDecimal rate = BigDecimal.valueOf(5.3);
        BigDecimal convertedAmount = amount.multiply(rate);

        ArgumentCaptor<TransactionEntity> transactionArgumentCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        transactionService.createTransaction(from, to, amount , rate);
        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        verify(transactionRepository).save(any());

        TransactionEntity capturedTransactionEntity = transactionArgumentCaptor.getValue();
        Assertions.assertEquals(from,capturedTransactionEntity.getSourceCurrency());
        Assertions.assertEquals(to,capturedTransactionEntity.getTargetCurrency());
        Assertions.assertEquals(amount,capturedTransactionEntity.getSourceAmount());
        Assertions.assertEquals(convertedAmount,capturedTransactionEntity.getTargetAmount());
    }

    @Test
    public void shouldFindTransactionByTransactionId() {
        Mockito.when(transactionRepository.findByTransactionId("11111")).thenReturn(Optional.of(new TransactionEntity()));
        transactionService.findByTransactionId("11111");
        verify(transactionRepository).findByTransactionId(any());
    }

    @Test
    public void shouldThrowExceptionIfTransactionIsNotFoundByTransactionId() {
        AssertionsForClassTypes.assertThatThrownBy(() -> transactionService.findByTransactionId("2222"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(BusinessExceptions.TRANSACTION_NOT_FOUND_BY_ID.getMessage());
        verify(transactionRepository).findByTransactionId(any());
    }

    @Test
    public void shouldFindAllTransactionsByTransactionDateBetweenDateWithPage() {
        Mockito.when(transactionRepository.findAllByTransactionDateBetween(any(),any(),any())).thenReturn(Page.empty());
        transactionService.findAllByTransactionDateBetweenWithPage(any(), any(), any());
        verify(transactionRepository).findAllByTransactionDateBetween(any(),any(),any());
    }


}
