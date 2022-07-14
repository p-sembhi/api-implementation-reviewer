package com.elsevier.apiimplementationreviewer.csv;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// This class extends functionality from CSVGenerator (parent class) and creates the header for document csv file
public class DocumentCSVGenerator extends CSVGenerator {

    public DocumentCSVGenerator(BufferedOutputStream output) throws IOException {
        this.output = output;
        this.output.write("DocumentID, Citation_Count, Implementation \n".getBytes(StandardCharsets.UTF_8));
    }
}
