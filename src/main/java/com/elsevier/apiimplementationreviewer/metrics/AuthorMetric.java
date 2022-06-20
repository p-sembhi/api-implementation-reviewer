package com.elsevier.apiimplementationreviewer.metrics;

import java.util.ArrayList;


// contains the author metric objects used by MetricReviewer (for now)
public class AuthorMetric implements Metric{
    public String id;
    public int totalCitedBy;
    public int totalCoAuthors;
    public int totalCitations;
    public int totalPapers;
    public ArrayList<String> coAuthorIds = new ArrayList<>();
    public int hindex;


    public AuthorMetric(String id, int hindex, int totalCitedBy, int totalCoAuthors){
        this.id = id;
        this.hindex = hindex;
        this.totalCitedBy = totalCitedBy;
        this.totalCoAuthors = totalCoAuthors;
        this.totalCitations = totalCitations;
        this.totalPapers = totalPapers;
        this.coAuthorIds = coAuthorIds;
    }


    public String toCSVString(){
       return String.format(
                "%s, %d, %d, %d\n",
                id,
                totalCitedBy,
                hindex,
                totalCoAuthors);
    }
}
