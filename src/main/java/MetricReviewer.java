import apiimplementationreviewer.csv.CSVGenerator;
import com.elsevier.apiimplementationreviewer.Neo4jClient;
import com.elsevier.apiimplementationreviewer.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.cypher.internal.parser.javacc.ParseException;
import org.neo4j.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Properties;

//responsible for running the project (main file)
public class MetricReviewer {

    private static final Logger logger = LoggerFactory.getLogger(MetricReviewer.class);

    public static void main( String... args ) throws Exception {
        Neo4jClient neo4jClient = new Neo4jClient("bolt://neo4j-cluster.cert.scopussearch.net:7687", "neo4j", "changeme", true, "full-4-may");
        Author author = new Author(neo4jClient);
        String id = "7005886441"; //want to change this to an array of ids
        int totalCitedBy = author.totalCitedBy(id);
        int totalCoAuthorCount = author.totalCoAuthorCount(id);
        int totalHIndexCount = author.totalHIndexCount(id);


        AuthorMetric authorQuery = new AuthorMetric(id, totalHIndexCount, totalCitedBy, totalCoAuthorCount);

        System.out.println(authorQuery.hindex);
        Document document = new Document(neo4jClient);
        int totalDocCitedBy = document.totalDocCitedBy("0024937865");
        System.out.println("Total Cited-By Document Count: " + totalDocCitedBy);



    try{
        BufferedOutputStream output = new BufferedOutputStream( new FileOutputStream(String.format("%s/%s", "reports", System.currentTimeMillis()) + ".csv"));
        CSVGenerator csv = new CSVGenerator (output);
        csv.appendAuthor(authorQuery);
        output.flush();
    }
    catch (
    FileNotFoundException exp) {
        // oops, something went wrong
        logger.error("Parsing failed.  Reason: " + exp.getMessage());
    }

    }
}

//create two authormetric classes (one with all of the metrics and the other one with the (JSON) metrics )
//look at generating a csv file to show the citation count for rest and cypher
