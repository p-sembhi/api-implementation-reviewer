package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.metrics.Metric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Session;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.print.Doc;

@Testcontainers
public class Neo4jDocumentTest {

    public static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(DockerImageName.parse("neo4j:4.2.6"))
            //inmemory (docker)
            .withAdminPassword("streams")
            .withReuse(true);

    static {
        if (!neo4jContainer.isRunning()) {
            neo4jContainer.start();
        }
    }

    Neo4jClient neo4jClient = new Neo4jClient(neo4jContainer.getBoltUrl(), "neo4j",
            "streams", false, "neo4j");

    @BeforeEach
    public void beforeEach(){
      Session session = neo4jClient.getSession(AccessMode.WRITE);
      session.run("CREATE (:Work {ID:'111'})");
        session.run("CREATE (:Work {ID:'222'})");
        session.run("MATCH (a:Work),(b:Work) WHERE a.ID = '111' AND b.ID = '222' CREATE (a)<-[r:references]-(b) RETURN type(r)");
    }

    @Test
    public void canCreateMetricCSVString(){
        Document document = new Document(neo4jClient);
        Metric metric = document.getMetric("111");
        Assertions.assertEquals("111, 1", metric.toCSVString()); //we expect to see a work with id 111 with 1
        // citation count (from work 222)
    }

    @Test
    public void canHandleNonExistentID(){
        Document document = new Document(neo4jClient);
        Metric metric = document.getMetric("333");
        Assertions.assertEquals("333, 0", metric.toCSVString()); //we expect to see a work with id 111 with 1
        // citation count (from work 222)
    } // from this test we want to now generate a log that can filter out incorrect ids and add blank space in csv file
    //if login does not work use Systemoutprintline

//    public void canGetTotalCitedByCount(){
//        Document document = new Document(neo4jClient);
//
//    }


    //write test but for authorNeo4j and 2 test for the restapi (we will want to mock the rest api using
    // mockito)
    //run code coverage at end to see percent covered
    //figure out logging asap
    //figure out how to create maven jar
    //DB credentials should not be hard coded.
}
