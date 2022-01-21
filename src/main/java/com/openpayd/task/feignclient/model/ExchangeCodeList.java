package com.openpayd.task.feignclient.model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ExchangeCodeList {
    public ArrayList<ArrayList<String>> supported_codes;

    public void setSupportedCodes(ArrayList<ArrayList<String>> supportedCodes){
        this.supported_codes = supportedCodes;
    }

    public Map<String, String> getSupportedCodes(){
        Map<String, String> codeMap = new HashMap<>();
        for(ArrayList<String> code : supported_codes){
            codeMap.put(code.get(0), code.get(1));
        }
        return codeMap;
    }
}
