package com.openpayd.task.feignclient.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ExchangeRate {
    @JsonIgnore
    private String base_code;
    @JsonIgnore
    private String target_code;
    @JsonIgnore
    private double conversion_rate;

    @JsonSetter(value = "base_code")
    public void setBaseCode(String base_code) {
        this.base_code = base_code;
    }

    @JsonSetter(value = "target_code")
    public void setTargetCode(String target_code) {
        this.target_code = target_code;
    }

    @JsonSetter(value = "conversion_rate")
    public void setConversionRate(double conversion_rate) {
        this.conversion_rate = conversion_rate;
    }

    @JsonGetter(value = "baseCode")
    public String getBaseCode() {
        return base_code;
    }

    @JsonGetter(value = "targetCode")
    public String getTargetCode() {
        return target_code;
    }

    @JsonGetter(value = "conversionRate")
    public double getConversionRate() {
        return conversion_rate;
    }
}
