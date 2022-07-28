package com.elsevier.apiimplementationreviewer.metrics;

import java.util.ArrayList;
import java.util.List;

public class DocumentMetric implements Metric{

    public String id;
    public int totalCitedBy;
    public List<String> citedByIds = new ArrayList<>();

    public DocumentMetric(){}

    public DocumentMetric(String id, int totalCitedBy, List<String> citedByIds){
        this.id = id;
        this.totalCitedBy = totalCitedBy;
        this.citedByIds = citedByIds;
    }

    public String toCSVString(){
        return String.format(
                "%s, %d, %s",
                id,
                totalCitedBy,
                citedByIds);
    }

    public String getId() {
        return id;
    }

    public int getTotalCitedBy() {
        return totalCitedBy;
    }

    public List<String> getCitedByIds() {
        return citedByIds;
    }
}
