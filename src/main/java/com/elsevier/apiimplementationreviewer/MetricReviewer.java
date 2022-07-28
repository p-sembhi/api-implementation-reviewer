package com.elsevier.apiimplementationreviewer;

import com.elsevier.apiimplementationreviewer.csv.AuthorCSVGenerator;
import com.elsevier.apiimplementationreviewer.csv.DocumentCSVGenerator;
import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.helper.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.metrics.Metric;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

//responsible for running the project (main file)
public class MetricReviewer {

    private static final Logger log = LoggerFactory.getLogger(MetricReviewer.class);
    private static final String appProperties = "src/main/resources/application.properties";
    private static final String reportsDirectory = "reports";

    public static final Option type = Option.builder("type")
            .argName("type")
            .hasArg()
            .desc("[author,document]")
            .build();

    public static final Options options = new Options()
            .addOption(type);


    public static void main(String... args) throws Exception {
        log.info(String.format("Running with args: %s", Arrays.toString(args)));

        CommandLineParser parser = new DefaultParser();

        try { //look into try catch with parameters vs resources
            InputStream input = new FileInputStream(appProperties);
            Properties properties = new Properties();
            properties.load(input);

            String dbName = properties.getProperty("neo4j.dbname");
            String dbUri = properties.getProperty("neo4j.driver.uri");
            String dbUsername = properties.getProperty("neo4j.driver.authentication.username");
            String dbPassword = properties.getProperty("neo4j.driver.authentication.password");
            String restEndpoint = properties.getProperty("api.rest.endpoint");

            Neo4jClient neo4jClient = new Neo4jClient(dbUri, dbUsername, dbPassword, true, dbName);
            ObjectMapper objectMapper = new ObjectMapper();
            HttpClient httpClient = HttpClient.newBuilder().build();
            RestApiFetcher restApiFetcher = new RestApiFetcher(objectMapper, httpClient, restEndpoint); //restapifetcher object



            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(type)) {

                File directory = new File(reportsDirectory);
                if (!directory.exists()){
                    log.debug("Creating reports directory.");
                    directory.mkdir();
                } else {
                    log.debug("Reports directory already exists.");
                }

                List<String> ids = line.getArgList();

                switch (line.getOptionValue(type)) {
                    case "author":
                        Author author = new Author(neo4jClient);
                        String authPath = String.format("%s/%s-%s.csv", reportsDirectory, "author", System.currentTimeMillis());
                        BufferedOutputStream authOutput = new BufferedOutputStream(new FileOutputStream(authPath));
                        AuthorCSVGenerator authCsv = new AuthorCSVGenerator(authOutput);
                        for (String id : ids) {
                            Metric authMetric = restApiFetcher.getAuthorMetric(id);
                            if (authMetric != null){
                                authCsv.appendMetrics(author.getMetric(id), authMetric);
                            } else {
                                log.warn(String.format("This id: %s does not exist", id));
                            }
                        }

                        authOutput.flush();
                        log.info(String.format("Created file: %s", authPath));
                        break;

                    case "document":
                        Document document = new Document(neo4jClient);
                        String docPath = String.format("%s/%s-%s.csv", reportsDirectory, "document", System.currentTimeMillis());
                        BufferedOutputStream docOutput = new BufferedOutputStream(new FileOutputStream(docPath));
                        DocumentCSVGenerator docCsv = new DocumentCSVGenerator(docOutput);
                        for (String id : ids) {
                            Metric docMetric = restApiFetcher.getDocumentMetric(id);
                            if (docMetric != null){
                                docCsv.appendMetrics(document.getMetric(id), docMetric);
                            } else {
                                log.warn(String.format("This id: %s does not exist", id));
                            }
                        }
                        docOutput.flush();
                        log.info(String.format("Created file: %s", docPath));
                        break;

                    default:
                        log.error(String.format("Invalid type, %s, can only be [author,document]", line.getOptionValue(type)));
                }

            } else {
                log.error("A type is required");
            }
        } catch (Exception exp) {
            log.error("Failed to run, reason: " + exp);
        }
    }
}

//create two authormetric classes (one with all of the metrics and the other one with the (JSON) metrics ) ->DONE
//look at generating a csv file to show the citation count for rest and cypher ->DONE
//Look into formatting CSV file (more readable) -> DONE?
//Pull in result from rest API endpoints (author / document) -> DONE
//look at pulling in id's from graph (consider number of id's and how random they are (number of nodes and coauthors))
//Start adding GraphQL queries?



