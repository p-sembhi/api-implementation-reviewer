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
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class Neo4jAuthorTest {
    public static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(DockerImageName.parse("neo4j:4.2.6"))
            .withPlugins(MountableFile.forClasspathResource("plugins/citation-graph-api.jar"))
            .withAdminPassword("streams")
            .withReuse(true);

    static {
        if (!neo4jContainer.isRunning()) {
            neo4jContainer.start();
        }
    }

    Neo4jClient neo4jClient = new Neo4jClient(neo4jContainer.getBoltUrl(), "neo4j", "streams", false, "neo4j");

    @BeforeEach
    public void beforeEach() {
        Session session = neo4jClient.getSession(AccessMode.WRITE);
        session.run("MATCH (n) DETACH DELETE n");
        session.run("CREATE (:Person {ID: '111'})");
        session.run("CREATE (:Person {ID: '222'})");
        session.run("CREATE (:Person {ID: '333'})");
        session.run("CREATE (:Work {ID: '444'})");
        session.run("CREATE (:Work {ID: '555'})");
        session.run("CREATE (:Work {ID: '666'})");
        session.run("CREATE (:Work {ID: '777'})");
        session.run(
                "MATCH (a:Person), (b:Work) WHERE a.ID = '111' AND b.ID = '444' CREATE (a)-[r:authorOf]->(b) RETURN " +
                        "type (r)");
        session.run(
                "MATCH (a:Person), (b:Work) WHERE a.ID = '333' AND b.ID = '444' CREATE (a)-[r:authorOf]->(b) RETURN " +
                        "type (r)");
        session.run(
                "MATCH (a:Work),(b:Work) WHERE a.ID = '444' AND b.ID = '555' CREATE (a)<-[r:references]-(b) RETURN " +
                        "type(r)");
        session.run(
                "MATCH (a:Work),(b:Work) WHERE a.ID = '444' AND b.ID = '666' CREATE (a)<-[r:references]-(b) RETURN " +
                        "type(r)");
        session.run(
                "MATCH (a:Work),(b:Work) WHERE a.ID = '444' AND b.ID = '777' CREATE (a)<-[r:references]-(b) RETURN " +
                        "type(r)");
        session.run(
                "MATCH (a:Person), (b:Work) WHERE a.ID = '222' AND b.ID = '555' CREATE (a)-[r:authorOf]->(b) RETURN " +
                        "type (r)");
    }

    @Test
    public void canCreateMetricCSVString() {
        Author author = new Author(neo4jClient);
        Metric metric = author.getMetric("111");
        Assertions.assertEquals("111, 3, 1, 1", metric.toCSVString());
    }

    @Test
    public void canHandleNonExistentID() {
        Author author = new Author(neo4jClient);
        Metric metric = author.getMetric("888");
        Assertions.assertEquals("888, 0, 0, 0", metric.toCSVString());
    }
}
