package com.openpayd.task.service;

import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.exception.BusinessExceptions;
import com.openpayd.task.repository.TransactionRepository;
import com.openpayd.task.repository.base.EntityRepository;
import com.openpayd.task.service.base.EntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TransactionService extends EntityService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository, EntityRepository entityRepository) {
        super(entityRepository);
        this.transactionRepository = transactionRepository;
    }

    public TransactionEntity createTransaction(String from, String to, BigDecimal amount, BigDecimal exchangeRate) {
        BigDecimal convertedAmount = amount.multiply(exchangeRate);
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setSourceAmount(amount);
        transactionEntity.setTargetAmount(convertedAmount);
        transactionEntity.setSourceCurrency(from);
        transactionEntity.setTargetCurrency(to);
        transactionEntity.setRate(exchangeRate);
        transactionEntity = transactionRepository.save(transactionEntity);
        return transactionEntity;
    }

    public TransactionEntity findByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).orElseThrow(() -> BusinessExceptions.TRANSACTION_NOT_FOUND_BY_ID);
    }

    public Page<TransactionEntity> findAllByTransactionDateBetweenWithPage(Date startDate, Date endDate, Pageable pageable) {
        return transactionRepository.findAllByTransactionDateBetween(startDate,endDate,pageable);
    }
}
