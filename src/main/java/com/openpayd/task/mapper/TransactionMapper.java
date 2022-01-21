package com.openpayd.task.mapper;

import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    public Transaction toModel(TransactionEntity transactionEntity){
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

    public List<Transaction> toModelList(List<TransactionEntity> transactionEntities){
        if(CollectionUtils.isEmpty(transactionEntities))
            return new ArrayList<>();
        return transactionEntities.stream().map(transactionEntity -> toModel(transactionEntity)).collect(Collectors.toList());
    }
}
