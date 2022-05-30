import com.elsevier.apiimplementationreviewer.Neo4jClient;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import org.neo4j.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;

public class MetricReviewer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricReviewer.class);

    public static void main( String... args ) throws Exception {
        try (Neo4jClient client = new Neo4jClient("bolt://neo4j-cluster.dev.scopussearch.net:7687", "neo4j", "changeme", true, "full-14-april")) {
            Author author = new Author(client);
            int totalCitedBy = author.totalCitedBy("7005886441");
            System.out.println("Total cited by: " + totalCitedBy);

            Document document = new Document(client);
            int totalDocCitedBy = document.totalDocCitedBy("0024937865");
            System.out.println("Total cited by: " + totalDocCitedBy);
        }
    }

}

