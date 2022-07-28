package com.elsevier.apiimplementationreviewer.helper;

import com.elsevier.apiimplementationreviewer.csv.CSVGenerator;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


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
        AuthorMetric metric = getMetric(URI.create(endPoint + "/authors/" + id), AuthorMetric.class);
        if (metric != null) {
            metric.id = id;
        }

        return metric;
    }

    public DocumentMetric getDocumentMetric(String id) throws IOException, InterruptedException {
        DocumentMetric metric = getMetric(URI.create(endPoint + "/documents/" + id), DocumentMetric.class);
        if (metric != null) {
            metric.id = id;
        }

        return metric;
    }

    private <T> T getMetric(URI uri, Class<T> valueType) throws IOException, InterruptedException {
        T metric = null;
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            metric = objectMapper.readValue(response.body(), valueType);
        }else {
            logger.warn(String.format("Got status code: %d for uri: %s", response.statusCode(), uri));
        }

        return metric;
    }
}
