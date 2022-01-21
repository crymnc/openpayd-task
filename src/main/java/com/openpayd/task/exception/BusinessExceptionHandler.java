package com.openpayd.task.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.task.feignclient.model.ExchangeRateApiError;
import feign.FeignException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class BusinessExceptionHandler {
    private static final Logger logger = LogManager.getLogger(BusinessExceptionHandler.class);

    @Value("${openpayd.isDebug}")
    private boolean isDebug;

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity throwBusinessException(BusinessException businessException) {
        logger.error(businessException);
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(businessException.getMessage())
                .debugMessage(isDebug ? ExceptionUtils.getStackTrace(businessException) : null)
                .timestamp(LocalDateTime.now())
                .code(businessException.getCode())
                .build();
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity throwFeignException(FeignException feignException) {
        logger.error(feignException);
        ExchangeRateApiError exchangeRateApiError = null;
        try {
            exchangeRateApiError = new ObjectMapper().readValue(feignException.contentUTF8(),ExchangeRateApiError.class);
        } catch (JsonProcessingException e) {}

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exchangeRateApiError.errorType)
                .debugMessage(isDebug ? ExceptionUtils.getStackTrace(feignException):null)
                .timestamp(LocalDateTime.now())
                .code("500")
                .build();
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity throwUnexpectedException(Exception exception) {
        logger.error(exception);
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .debugMessage(isDebug ? ExceptionUtils.getStackTrace(exception):null)
                .timestamp(LocalDateTime.now())
                .code("500")
                .build();
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
