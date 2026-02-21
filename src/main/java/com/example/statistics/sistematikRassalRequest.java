package com.example.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class sistematikRassalRequest {

    @JsonProperty("N")
    private int total;

    @JsonProperty("n")
    private int n;


    public int getN() {
        return n;
    }
    public int getTotal() {
        return total;
    }

}
