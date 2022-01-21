package com.openpayd.task.repository;

import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.repository.base.EntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TransactionRepository extends EntityRepository<TransactionEntity> {

    Optional<TransactionEntity> findByTransactionId(String transactionId);

    Page<TransactionEntity> findAllByTransactionDateBetween(Date startDate, Date endDate, Pageable pageable);
}
