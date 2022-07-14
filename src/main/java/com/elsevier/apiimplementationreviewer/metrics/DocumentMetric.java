package com.elsevier.apiimplementationreviewer.helper.metrics;

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
}


