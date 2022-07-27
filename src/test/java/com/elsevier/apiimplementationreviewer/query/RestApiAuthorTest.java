package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.helper.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestApiAuthorTest {

    private static final String AUTHOR_ID = "1122334456";
    private static final String ENDPOINT = "http://restcitation";

    RestApiFetcher fetcher;

    @Mock
    HttpClient client;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    Logger logger;

    @BeforeEach
    void setup() {
        fetcher = new RestApiFetcher(objectMapper, client, ENDPOINT);
    }

    @Test
    void getAuthorMetric () throws IOException, InterruptedException {
        HttpResponse<Object> response = mock(HttpResponse.class); //we need to create a mock response object since we
        // can not mock a string.
      when(client.send(any(), any())).thenReturn(response); //when the send method is called on our mock client
        // object, we will return the response body (HttpResponse)
        when(response.body()).thenReturn("response"); //when response.body is called, return the mock object
        AuthorMetric authorMetric = new AuthorMetric(AUTHOR_ID, 1122, 30, 6); // Create authormetric object, filling
        // out the constructor parameters using our newly created authorId and int values.
        when(objectMapper.readValue("response", AuthorMetric.class)).thenReturn(authorMetric);
        //when objectMapper reads the value of our response body, we will simply return the new mock author metric
        // object.

        AuthorMetric metric = fetcher.getAuthorMetric(AUTHOR_ID); // call our method on the RestApiFetcher
        // class

        assertNotNull(metric);
        assertEquals(AUTHOR_ID, metric.getId());
        assertEquals(1122, metric.getHindex());
        assertEquals(30, metric.getTotalCitedBy());
        assertEquals(6, metric.getTotalCoAuthors());
    }

}
