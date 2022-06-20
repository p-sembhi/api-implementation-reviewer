package com.elsevier.apiimplementationreviewer.metrics;

public class DocumentMetric implements Metric{
    public String id;
    public int totalDocCitedBy;

    public DocumentMetric(){}

    public DocumentMetric(String id, int totalDocCitedBy){
        this.id = id;
        this.totalDocCitedBy = totalDocCitedBy;
    }
    public String toCSVString(){
        return String.format(
                "%s, %d\n",
                id,
                totalDocCitedBy);
    }
}


