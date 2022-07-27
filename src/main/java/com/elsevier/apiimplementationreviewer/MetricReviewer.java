package com.elsevier.apiimplementationreviewer;

import com.elsevier.apiimplementationreviewer.csv.AuthorCSVGenerator;
import com.elsevier.apiimplementationreviewer.csv.DocumentCSVGenerator;
import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.helper.RestApiFetcher;
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
import java.util.List;
import java.util.Properties;

//responsible for running the project (main file)
public class MetricReviewer {

    private static final Logger log = LoggerFactory.getLogger(MetricReviewer.class);
    private static final String appProperties = "src/main/resources/application.properties";


    public static final Option type = Option.builder("type")
            .argName("type")
            .hasArg()
            .desc("[author,document]")
            .build();

    public static final Options options = new Options()
            .addOption(type);


    public static void main(String... args) throws Exception {

        CommandLineParser parser = new DefaultParser();

//        logger.error("Hello World" ); fix this later on look into slf4j error message
        log.debug("Hello");
        log.info("info logger");
//use slf4j for logging if log4j still doesn't work

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
            RestApiFetcher restApiFetcher =
                    new RestApiFetcher(objectMapper, httpClient, restEndpoint); //restapifetcher object

            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(type)) {

                List<String> ids = line.getArgList();
                //look at bufferedoutputstream memory for portfolio

                switch (line.getOptionValue(type)) {
                    case "author":
                        Author author = new Author(neo4jClient);
                        BufferedOutputStream output = new BufferedOutputStream(
                                new FileOutputStream(
                                        String.format("%s/%s-%s", "reports", "author", System.currentTimeMillis()) +
                                                ".csv"));
                        AuthorCSVGenerator csv = new AuthorCSVGenerator(output); //creating a new object
                        for (String id : ids) {
                            csv.appendMetrics(author.getMetric(id), restApiFetcher.getAuthorMetric(id));
                        }
                        output.flush();

                        break;

                    case "document":
                        Document document = new Document(neo4jClient);
                        BufferedOutputStream docOutput = new BufferedOutputStream(
                                new FileOutputStream(
                                        String.format("%s/%s-%s", "reports", "document",
                                                System.currentTimeMillis()) +
                                                ".csv"));
                        DocumentCSVGenerator docCsv = new DocumentCSVGenerator(docOutput);
                        for (String id : ids) {
                            docCsv.appendMetrics(document.getMetric(id),
                                    restApiFetcher.getDocumentMetric(id));
                        }
                        docOutput.flush();

                        break;


                    default:
                        System.err.format("%s, invalid type, can be [author,document]", type.getArgName());
                }

            } else {
                System.err.format("type is required");
            }


        } catch (
                FileNotFoundException exp) {
            // oops, something went wrong
            log.error("Parsing failed.  Reason: " + exp.getMessage());

        }
    }
}

//create two authormetric classes (one with all of the metrics and the other one with the (JSON) metrics ) ->DONE
//look at generating a csv file to show the citation count for rest and cypher ->DONE
//Look into formatting CSV file (more readable) -> DONE?
//Pull in result from rest API endpoints (author / document) -> DONE
//look at pulling in id's from graph (consider number of id's and how random they are (number of nodes and coauthors))
//Start adding GraphQL queries?



