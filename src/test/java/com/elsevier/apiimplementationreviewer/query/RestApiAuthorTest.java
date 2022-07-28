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

import static org.junit.jupiter.api.Assertions.*;
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

    @BeforeEach
    void setup() {
        fetcher = new RestApiFetcher(new ObjectMapper(), client, ENDPOINT);
    }

    @Test
    void getAuthorMetric () throws IOException, InterruptedException {
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(client.send(any(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"totalCitedBy\":1,\"totalCoAuthors\":3,\"hindex\":20}");

        AuthorMetric metric = fetcher.getAuthorMetric(AUTHOR_ID);

        assertNotNull(metric);
        assertEquals(1, metric.getTotalCitedBy());
        assertEquals(3, metric.getTotalCoAuthors());
        assertEquals(20, metric.getHindex());
        assertEquals(AUTHOR_ID, metric.getId());
        assertEquals(AUTHOR_ID+", 1, 20, 3", metric.toCSVString());
    }

    @Test
    void canHandleNotFoundId() throws IOException, InterruptedException {
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(client.send(any(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(404);

        AuthorMetric metric = fetcher.getAuthorMetric(AUTHOR_ID);
        assertNull(metric);
    }
}
