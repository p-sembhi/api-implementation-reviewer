package com.elsevier.apiimplementationreviewer.metrics;

import java.util.ArrayList;

public class AuthorMetric {
    public int totalCitedBy;
    public int totalCoAuthors;
    public int totalCitations;
    public int totalPapers;
    public ArrayList<String> coAuthorIds = new ArrayList<>();
    public int hindex;

    public AuthorMetric(int hindex, int totalCitedBy, int totalCoAuthors){
        this.hindex = hindex;
        this.totalCitedBy = totalCitedBy;
        this.totalCoAuthors = totalCoAuthors;
        this.totalCitations = totalCitations;
        this.totalPapers = totalPapers;
        this.coAuthorIds = coAuthorIds;
    }
}

