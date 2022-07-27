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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        AuthorMetric rAuthor = null;
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
            String test = response.body();
            rDoc = objectMapper.readValue(response.body(), DocumentMetric.class);
            if (rDoc != null){
                rDoc.id = id;
            }

        } catch (Exception e) {
            logger.error(String.format("Error processing %s, %s", id, e));
        }
        return rDoc;
    }
}
