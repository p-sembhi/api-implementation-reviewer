package com.elsevier.apiimplementationreviewer.metrics;

import reactor.util.annotation.Nullable;

import java.util.ArrayList;

public class DocumentMetric implements Metric{

    public String id;
    public int totalCitedBy;
    public ArrayList<String> citedByIds = new ArrayList<>();

    public DocumentMetric(){}

    public DocumentMetric(String id, int totalCitedBy){
        this.id = id;
        this.totalCitedBy = totalCitedBy;
    }
    public String toCSVString(){
        return String.format(
                "%s, %d",
                id,
                totalCitedBy);
    }

    public String getId() {
        return id;
    }

    public int getTotalCitedBy() {
        return totalCitedBy;
    }

    public ArrayList<String> getCitedByIds() {
        return citedByIds;
    }
}
