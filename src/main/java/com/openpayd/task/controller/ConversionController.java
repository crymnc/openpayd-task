package com.openpayd.task.controller;

import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.feignclient.ExchangeRateApi;
import com.openpayd.task.feignclient.model.ExchangeRate;
import com.openpayd.task.mapper.TransactionMapper;
import com.openpayd.task.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Conversion Controller")
@ApiResponses(value={
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "404", description = "Page Not Found", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema(hidden = true))})
})
public class ConversionController {

    private ExchangeRateApi exchangeRateApi;
    private TransactionService transactionService;
    private TransactionMapper transactionMapper;

    public ConversionController(ExchangeRateApi exchangeRateApi, TransactionService transactionService, TransactionMapper transactionMapper){
        this.exchangeRateApi = exchangeRateApi;
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/from/{from}/to/{to}/amount/{amount}")
    @Operation(summary = "Convert given amount to desired currency")
    @ApiResponse(responseCode = "200", description = "Transaction information is returned", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))})
    public ResponseEntity convertCurrencies(@PathVariable(name="from") String from, @PathVariable(name="to") String to, @PathVariable(name="amount") BigDecimal amount){
        ExchangeRate exchangeRate = exchangeRateApi.getExchangeRate(from,to);
        TransactionEntity transactionEntity = transactionService.createTransaction(from, to, amount, BigDecimal.valueOf(exchangeRate.getConversionRate()));
        Transaction transaction = transactionMapper.toModel(transactionEntity);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }
}
