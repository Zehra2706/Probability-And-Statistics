package com.example.statistics;
import java.util.List;

public class tabakasalRequests {



    // private int totalPopulation;   // 0 → N
    // private int sampleCount;       // kaç tane seçilecek
    // private List<Integer> strataPercentages; // %


    private int totalPopulation;
    private int sampleCount;
    private List<Integer> strataPercentages;

    public int gettotalPopulation() {
        return totalPopulation;
    }

    public void settotalPopulation(int totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public int getsampleCount() {
        return sampleCount;
    }

    public void setsampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public List<Integer> getstrataPercentages() {
        return strataPercentages;
    }

    public void setstrataPercentages(List<Integer> strataPercentages) {
        this.strataPercentages = strataPercentages;
    }


}