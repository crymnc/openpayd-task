package com.openpayd.task.feignclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateApiError {
    public String result;
    public String documentation;
    @JsonProperty("terms-of-use")
    public String termsOfUse;
    @JsonProperty("error-type")
    public String errorType;
}
