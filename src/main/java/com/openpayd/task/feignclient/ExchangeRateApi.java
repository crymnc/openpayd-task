package com.openpayd.task.feignclient;

import com.openpayd.task.feignclient.model.ExchangeCodeList;
import com.openpayd.task.feignclient.model.ExchangeRate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${openpayd.exchange-rate-api.url}", name="exchange-rates-api")
public interface ExchangeRateApi {

    String ACCESS_KEY = "2df898732a5ea9a62bd379a3";

    @GetMapping(ACCESS_KEY+"/codes")
    @Cacheable(value = "codes")
    ExchangeCodeList getCodes();

    @GetMapping(ACCESS_KEY+"/pair/{from}/{to}")
    @Cacheable(value = "exchangeRates")
    ExchangeRate getExchangeRate(@PathVariable(name="from") String from, @PathVariable(name="to") String to);


}
