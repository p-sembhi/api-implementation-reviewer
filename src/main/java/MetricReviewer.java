import com.elsevier.apiimplementationreviewer.Neo4jClient;
import com.elsevier.apiimplementationreviewer.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.Closeable;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Properties;

public class MetricReviewer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricReviewer.class);

    public static void main( String... args ) throws Exception {
        try (Neo4jClient neo4jClient = new Neo4jClient("bolt://neo4j-cluster.dev.scopussearch.net:7687", "neo4j", "changeme", true, "full-14-april")
        )
        {
            Author author = new Author(neo4jClient);
            int totalCitedBy = author.totalCitedBy("7005886441");
            int totalCoAuthorCount = author.totalCoAuthorCount("7005886441");
            int totalHIndexCount = author.totalHIndexCount("7005886441");
            System.out.println("Total Cited-By Author Count: " + totalCitedBy);
            System.out.println("Total Co-Author Count: " + totalCoAuthorCount);
            System.out.println("Total H-Index Count: " + totalHIndexCount);

            AuthorMetric authorQuery = new AuthorMetric(totalHIndexCount, totalCitedBy, totalCoAuthorCount);

            System.out.println(authorQuery.hindex);
            Document document = new Document(neo4jClient);
            int totalDocCitedBy = document.totalDocCitedBy("0024937865");
            System.out.println("Total Cited-By Document Count: " + totalDocCitedBy);

            HttpClient httpClient = HttpClient.newBuilder().build();
            ObjectMapper mapper = new ObjectMapper();
            RestApiFetcher fetcher = new RestApiFetcher(mapper,httpClient, "https://citation-graph-api-refeed.dev.scopussearch.net/api/graphservice/authors");
            AuthorMetric authorApi = fetcher.getMetrics("7005886441"); // get rid of nullpointer error (should return more than hindexcount)
            System.out.println(authorApi.hindex);
        }

    }

}

//create two authormetric classes (one with all of the metrics and the other one with the (JSON) metrics )
