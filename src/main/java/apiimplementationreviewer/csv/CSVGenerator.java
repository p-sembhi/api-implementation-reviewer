package apiimplementationreviewer.csv;

import com.elsevier.apiimplementationreviewer.metrics.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// parent class for author and document generator (shares functionality)
public class CSVGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CSVGenerator.class);
    protected OutputStream output; // protected access modifier can be accessed safely from the class itself or
    // subclasses

    public void appendMetric(Metric metric)  {

        try{
            output.write(metric.toCSVString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex){
            logger.error("Failed to append author ", ex);
        }
    }
}
