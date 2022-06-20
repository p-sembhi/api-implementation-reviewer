package apiimplementationreviewer.csv;

import java.io.*;
import java.nio.charset.StandardCharsets;

// This class extends functionality from CSVGenerator (parent class) and creates the header for Author csv file
public class AuthorCSVGenerator extends CSVGenerator{

    public AuthorCSVGenerator(BufferedOutputStream output) throws IOException {
        this.output = output;
        this.output.write("ID , Query_CitedBy , Query_HIndex , Query_CoAuthor \n".getBytes(StandardCharsets.UTF_8));
    }
}