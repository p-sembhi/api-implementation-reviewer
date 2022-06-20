import apiimplementationreviewer.csv.AuthorCSVGenerator;
import apiimplementationreviewer.csv.DocumentCSVGenerator;
import com.elsevier.apiimplementationreviewer.Neo4jClient;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

//responsible for running the project (main file)
public class MetricReviewer {

    private static final Logger logger = LoggerFactory.getLogger(MetricReviewer.class);

    public static void main( String... args ) throws Exception {
        String authorId = "7005886441"; //want to change this to an array of ids
        String documentId = "0024937865";
        Neo4jClient neo4jClient = new Neo4jClient("bolt://neo4j-cluster.cert.scopussearch.net:7687", "neo4j",
                "changeme", true, "full-4-may");

        Author author = new Author(neo4jClient);
        Document document = new Document(neo4jClient);

        try {
            BufferedOutputStream output = new BufferedOutputStream(
                    new FileOutputStream(String.format("%s/%s-%s","reports","author", System.currentTimeMillis()) +
                            ".csv"));
            AuthorCSVGenerator csv = new AuthorCSVGenerator(output);
            csv.appendMetric(author.getMetric(authorId));
            output.flush();

            BufferedOutputStream docOutput = new BufferedOutputStream(
                    new FileOutputStream(String.format("%s/%s-%s", "reports","document", System.currentTimeMillis()) +
                            ".csv"));
            DocumentCSVGenerator docCsv = new DocumentCSVGenerator(docOutput);
            docCsv.appendMetric(document.getMetric(documentId));
            docOutput.flush();
        }
        catch (
            FileNotFoundException exp) {
                // oops, something went wrong
                logger.error("Parsing failed.  Reason: " + exp.getMessage());
            }
    }
}

//create two authormetric classes (one with all of the metrics and the other one with the (JSON) metrics ) ->DONE
//look at generating a csv file to show the citation count for rest and cypher ->DONE
//Look into formatting CSV file (more readable) -> DONE?
//Pull in result from rest API endpoints (author / document)
//look at pulling in id's from graph (consider number of id's and how random they are (number of nodes and coauthors))
//Start adding GraphQL queries?

