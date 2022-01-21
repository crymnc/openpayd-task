package com.openpayd.task.entity;

import com.openpayd.task.entity.base.BaseAuditEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity extends BaseAuditEntity {

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "source_currency")
    private String sourceCurrency;

    @Column(name = "target_currency")
    private String targetCurrency;

    @Column(name = "source_amount")
    private BigDecimal sourceAmount;

    @Column(name = "target_amount")
    private BigDecimal targetAmount;

    @Column(name = "rate")
    private BigDecimal rate;

    @PrePersist
    protected void setCreationParameters() {
        super.setCreationParameters();
        this.transactionDate = new Date();
        this.transactionId = UUID.randomUUID().toString();
    }
}
