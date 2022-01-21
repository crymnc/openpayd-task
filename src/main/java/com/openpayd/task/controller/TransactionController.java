package com.openpayd.task.controller;

import com.openpayd.task.domain.PageResponse;
import com.openpayd.task.domain.Transaction;
import com.openpayd.task.entity.TransactionEntity;
import com.openpayd.task.mapper.TransactionMapper;
import com.openpayd.task.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller")
@ApiResponses(value={
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "404", description = "Page Not Found", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema(hidden = true))})
})
public class TransactionController {

    private TransactionService transactionService;
    private TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("{transaction-id}")
    @Operation(summary = "Return Transaction With Transaction ID")
    @ApiResponse(responseCode = "200", description = "Returns transaction", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))})
    public ResponseEntity getTransactionById(@PathVariable(name = "transaction-id") String transactionId) {
        TransactionEntity transactionEntity = transactionService.findByTransactionId(transactionId);
        Transaction transaction = transactionMapper.toModel(transactionEntity);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @GetMapping(value = "/between/{start-date}/and/{end-date}")
    @Operation(summary = "Return Transactions Between Given Dates (yyyy-MM-dd)")
    @ApiResponse(responseCode = "200", description = "Returns transaction list between dates with page", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))})
    public ResponseEntity getTransactionsBetweenDates(@PathVariable(name = "start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                            @PathVariable(name = "end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                            @RequestParam(value = "page",defaultValue = "0") int page,
                                                            @RequestParam(value = "size",defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<TransactionEntity> transactionPage = transactionService.findAllByTransactionDateBetweenWithPage(startDate, endDate, pageable);
        List<Transaction> transactions = transactionMapper.toModelList(transactionPage.getContent());
        PageResponse pageResponse = PageResponse.builder()
                .currentPage(transactionPage.getNumber())
                .totalPages(transactionPage.getTotalPages())
                .totalElements(transactionPage.getTotalElements())
                .content(transactions).build();
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }
}
