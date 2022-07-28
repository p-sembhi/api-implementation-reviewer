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
        session.run("MATCH (n) DETACH DELETE n"); //detach means to remove relationships before deleting
        session.run("CREATE (:Work {ID:'111'})");
        session.run("CREATE (:Work {ID:'222'})");
        session.run("MATCH (a:Work),(b:Work) WHERE a.ID = '111' AND b.ID = '222' CREATE (a)<-[r:references]-(b) RETURN type(r)");
    }

    @Test
    public void canCreateMetricCSVString(){
        Document document = new Document(neo4jClient);
        Metric metric = document.getMetric("111");
        Assertions.assertEquals("111, 1, [222]", metric.toCSVString());
    }

    @Test
    public void canHandleNonExistentID(){
        Document document = new Document(neo4jClient);
        Metric metric = document.getMetric("333");
        Assertions.assertEquals("333, 0, []", metric.toCSVString());
    }
}
