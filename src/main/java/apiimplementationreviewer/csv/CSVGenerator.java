package apiimplementationreviewer.csv;

import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CSVGenerator.class);
    private OutputStream output;


    public CSVGenerator(BufferedOutputStream output) throws IOException {
//        new File(directoryName).mkdir();
        this.output = output;
        this.output.write("ID |, Query_HIndex, Query_CoAuthor, Query_CitedBy\n".getBytes(StandardCharsets.UTF_8));
    }


    public void appendAuthor(AuthorMetric authorMetric)  {
        try{
            output.write(String.format(
                    "%s, %d, %d, %d\n",
                    authorMetric.id,
                    authorMetric.hindex,
                    authorMetric.totalCoAuthors,
                    authorMetric.totalCitedBy).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex){
            logger.error("Failed to append author ", ex);
        }

    }

}
