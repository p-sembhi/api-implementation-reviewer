package com.elsevier.apiimplementationreviewer.metrics;

import java.util.ArrayList;

//used by both neo4j and restapi
// contains the author metric objects used by MetricReviewer (for now)
public class AuthorMetric implements Metric{
    public String id;
    public int totalCitedBy;
    public int totalCoAuthors;
    public int totalCitations;
    public int totalPapers;
    public ArrayList<String> coAuthorIds = new ArrayList<>();
    public int hindex;

public AuthorMetric(){}

    public AuthorMetric(String id, int hindex, int totalCitedBy, int totalCoAuthors){
        this.id = id;
        this.hindex = hindex;
        this.totalCitedBy = totalCitedBy;
        this.totalCoAuthors = totalCoAuthors;
        this.totalCitations = totalCitations; // add in later
        this.totalPapers = totalPapers;
        this.coAuthorIds = coAuthorIds;
    }


    public String toCSVString(){
       return String.format(
                "%s, %d, %d, %d",
                id,
                totalCitedBy,
                hindex,
                totalCoAuthors);
    }

    public String getId() {
        return id;
    }

    public int getTotalCitedBy() {
        return totalCitedBy;
    }

    public int getTotalCoAuthors() {
        return totalCoAuthors;
    }

    public int getHindex() {
        return hindex;
    }
}
