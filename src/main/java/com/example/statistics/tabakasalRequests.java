package com.example.statistics;
import java.util.List;

public class tabakasalRequests {

    private int totalSampleSize;
    private List<Integer> strataSizes;

    public int getTotalSampleSize() {
        return totalSampleSize;
    }

    public void setTotalSampleSize(int totalSampleSize) {
        this.totalSampleSize = totalSampleSize;
    }

    public List<Integer> getStrataSizes() {
        return strataSizes;
    }

    public void setStrataSizes(List<Integer> strataSizes) {
        this.strataSizes = strataSizes;
    }
}