package com.openpayd.task.controller;

import com.openpayd.task.feignclient.ExchangeRateApi;
import com.openpayd.task.feignclient.model.ExchangeCodeList;
import com.openpayd.task.feignclient.model.ExchangeRate;
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

import java.util.Map;

@RestController
@RequestMapping("/api/rates")
@Tag(name = "Rate Controller")
@ApiResponses(value={
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "404", description = "Page Not Found", content = {@Content(schema = @Schema(hidden = true))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(schema = @Schema(hidden = true))})
})
public class RateController {

    private ExchangeRateApi exchangeRateApi;

    public RateController(ExchangeRateApi exchangeRateApi){
        this.exchangeRateApi = exchangeRateApi;
    }

    @GetMapping("/codes")
    @Operation(summary = "Get All Available Currency Codes")
    @ApiResponse(responseCode = "200", description = "Returns all currency name, code pair", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))})
    public ResponseEntity getCodes(){
        ExchangeCodeList exchangeCodeList = exchangeRateApi.getCodes();
        return ResponseEntity.status(HttpStatus.OK).body(exchangeCodeList.getSupportedCodes());
    }

    @GetMapping("/from/{from}/to/{to}")
    @Operation(summary = "Get Exchange Rate For Given Currency Pair")
    @ApiResponse(responseCode = "200", description = "Returns exchange rate for given currency pair", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRate.class))})
    public ResponseEntity getExchangeRates(@PathVariable(name="from") String from, @PathVariable(name="to") String to){
        ExchangeRate exchangeRate = exchangeRateApi.getExchangeRate(from,to);
        return ResponseEntity.status(HttpStatus.OK).body(exchangeRate);
    }
}
