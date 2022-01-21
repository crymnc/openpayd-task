package com.openpayd.task.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Transaction {

    private String transactionId;
    private Date transactionDate;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal targetAmount;
    private BigDecimal sourceAmount;
    private BigDecimal rate;
}
