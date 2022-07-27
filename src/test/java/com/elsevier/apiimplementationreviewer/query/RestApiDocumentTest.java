package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.helper.RestApiFetcher;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RestApiDocumentTest {

    private static final String DOCUMENT_ID = "10998877";
    private static final String ENDPOINT = "http://citation";

    RestApiFetcher fetcher;

   @Mock
   HttpClient client;

   @BeforeEach
    void setup() {
       fetcher = new RestApiFetcher(new ObjectMapper(), client, ENDPOINT);
   }

    @Test
    void getDocumentMetrics () throws IOException, InterruptedException {
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(client.send(any(), any())).thenReturn(response);
        //try out 404 status code test here
        when(response.body()).thenReturn(
                "{\"citedByIds\":[\"85121047907\"],\"totalCitedBy\":10}");

        DocumentMetric metric = fetcher.getDocumentMetric(DOCUMENT_ID);

        assertNotNull(metric);
        assertEquals(10, metric.getTotalCitedBy());
        assertEquals(List.of("85121047907"), metric.getCitedByIds());
        assertEquals(DOCUMENT_ID, metric.getId());
    }
}

//test for 400 error. mock(HttpResponseStatusCode) if we get a 400 error we expect to see a id in teh csv with no
// metrics.

