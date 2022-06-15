package com.elsevier.apiimplementationreviewer;


import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

// responsible for fetching data from api
public class RestApiFetcher {

    private HttpClient client;
    private String endPoint;
    private ObjectMapper objectMapper;

    public RestApiFetcher(ObjectMapper objectMapper, HttpClient client, String endPoint) {
        this.objectMapper = objectMapper;
        this.client = client;
        this.endPoint = endPoint;
    }

    public AuthorMetric getMetrics(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endPoint + "/" + id)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //because we are using ofString we need <string>

        AuthorMetric r = null;
        try {
            r = Optional.of(objectMapper.readValue(response.body(), AuthorMetric.class)).get();
        } catch (IOException e) {
//            logger.error(String.format("Error processing %s, %s", id,e.toString()));
        }
        return r;
    }

}
