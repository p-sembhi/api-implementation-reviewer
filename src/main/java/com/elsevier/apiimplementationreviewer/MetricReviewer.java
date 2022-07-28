package com.elsevier.apiimplementationreviewer;

import com.elsevier.apiimplementationreviewer.csv.AuthorCSVGenerator;
import com.elsevier.apiimplementationreviewer.csv.DocumentCSVGenerator;
import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.helper.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.query.Author;
import com.elsevier.apiimplementationreviewer.query.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.http.HttpClient;
import java.util.List;

//responsible for running the project (main file)
public class MetricReviewer {

    private static final Logger logger = LoggerFactory.getLogger(MetricReviewer.class);

    public static final Option type = Option.builder("type")
            .argName("type")
            .hasArg()
            .desc("[author,document]")
            .build();

    public static final Options options = new Options()
            .addOption(type);


    public static void main(String... args) throws Exception {
        CommandLineParser parser = new DefaultParser();

//        String authorId = "7005886441"; //want to change this to an array of ids
//        String documentId = "0024937865";

        try {
            Neo4jClient neo4jClient = new Neo4jClient("bolt://neo4j-cluster.cert.scopussearch.net:7687", "neo4j",
                    "changeme", true, "full-4-may");
            ObjectMapper objectMapper = new ObjectMapper();
            HttpClient httpClient = HttpClient.newBuilder().build();
            String endpoint = "https://citation-graph-api-refeed.dev.scopussearch.net/api/graphservice";
            RestApiFetcher restApiFetcher =
                    new RestApiFetcher(objectMapper, httpClient, endpoint); //restapifetcher object

            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(type)) {

                List<String> ids = line.getArgList();
                for (String id : ids) {

                    switch (line.getOptionValue(type)) {
                        case "author":
                            Author author = new Author(neo4jClient);
                            BufferedOutputStream output = new BufferedOutputStream(
                                    new FileOutputStream(
                                            String.format("%s/%s-%s", "reports", "author", System.currentTimeMillis()) +
                                                    ".csv"));
                            AuthorCSVGenerator csv = new AuthorCSVGenerator(output);
                            csv.appendMetrics(author.getMetric(id), restApiFetcher.getAuthorMetric(id));
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
                            docCsv.appendMetrics(document.getMetric(id),
                                    restApiFetcher.getDocumentMetric(id));
                            docOutput.flush();

                            break;


                        default:
                            System.err.format("%s, invalid type, can be [author,document]", type.getArgName());
                    }
                }
            } else {
                System.err.format("type is required");
            }


        } catch (
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

//we received an error with the endpoint -no metrics found

