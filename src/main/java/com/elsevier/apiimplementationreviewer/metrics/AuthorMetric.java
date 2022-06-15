package com.elsevier.apiimplementationreviewer.metrics;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// contains the author metric objects used by MetricReviewer (for now)
public class AuthorMetric {
    public String id;
    public int totalCitedBy;
    public int totalCoAuthors;
    public int totalCitations;
    public int totalPapers;
    public ArrayList<String> coAuthorIds = new ArrayList<>();
    public int hindex;

    public AuthorMetric (){

    }

    public AuthorMetric(String id, int hindex, int totalCitedBy, int totalCoAuthors){
        this.id = id;
        this.hindex = hindex;
        this.totalCitedBy = totalCitedBy;
        this.totalCoAuthors = totalCoAuthors;
        this.totalCitations = totalCitations;
        this.totalPapers = totalPapers;
        this.coAuthorIds = coAuthorIds;
    }

//    public static void main(String[] args) throws IOException {
//
//        List<String[]> csvData = createCsvDataSimple();
//
//        // default all fields are enclosed in double quotes
//        // default separator is a comma
//        try (CSVWriter writer = new CSVWriter(new FileWriter("c:\\test\\test.csv"))) {
//            writer.writeAll(csvData);
//        }
//
//    }
//
//    private static List<String[]> createCsvDataSimple() {
//        String[] header = {"id", "name", "address", "phone"};
//        String[] record1 = {"1", "first name", "address 1", "11111"};
//        String[] record2 = {"2", "second name", "address 2", "22222"};
//
//        List<String[]> list = new ArrayList<>();
//        list.add(header);
//        list.add(record1);
//        list.add(record2);
//
//        return list;
//    }
}

