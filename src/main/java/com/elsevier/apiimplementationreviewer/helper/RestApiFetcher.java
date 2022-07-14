package com.elsevier.apiimplementationreviewer.helper;


import com.elsevier.apiimplementationreviewer.csv.CSVGenerator;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

// This class is responsible for fetching data from api

public class RestApiFetcher extends CSVGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RestApiFetcher.class);
    private final HttpClient client;
    private final String endPoint;
    private final ObjectMapper objectMapper;

    public RestApiFetcher(ObjectMapper objectMapper, HttpClient client, String endPoint) {
        this.objectMapper = objectMapper;
        this.client = client;
        this.endPoint = endPoint;
    }

    public AuthorMetric getAuthorMetric(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endPoint + "/authors/" + id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //because we are using ofString we need <string>

        AuthorMetric rAuthor = null; //set to null which is causing null pointer exception?
        try {
            rAuthor = Optional.of(objectMapper.readValue(response.body(), AuthorMetric.class)).get();
            rAuthor.id = id;
        } catch (IOException e) {
            logger.error(String.format("Error processing %s, %s", id, e));
        }

        return rAuthor;
    }

    public DocumentMetric getDocumentMetric(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endPoint + "/documents/" + id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        DocumentMetric rDoc = null;
        try {
            rDoc = Optional.of(objectMapper.readValue(response.body(), DocumentMetric.class)).get();
            rDoc.id = id;
        } catch (IOException e) {
            logger.error(String.format("Error processing %s, %s", id, e));
        }
        return rDoc;
    }
}

// try to get this to return author and doc metrics then and display on csv file.
//Create two seperate methods (one for author and one for documents)
// use append metric method?
// look at how i would interact with GraphQL endpoint (ask sarina how to use insomnia profile to access graphql)
// endpoint no longer works? look into why this is. Might have to use insomnia endpoint but would mean adding token
//  solution -New db that nancy created was not added to puppet repo so was pointing to old db which is why endpoint
//  stopped working.  
