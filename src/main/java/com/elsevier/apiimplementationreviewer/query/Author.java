package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.metrics.AuthorMetric;
import org.neo4j.driver.Record;

import java.util.Map;
//used by neo4j
public class Author implements QueryMetric {

    private final Neo4jClient neo4j;
    //this class is responsible for running queries and returning the result set

    public Author(Neo4jClient neo4j) {
        this.neo4j = neo4j;
    }

    public AuthorMetric getMetric(String id){
        return new AuthorMetric(
                id,
                totalHIndexCount(id),
                totalCitedBy(id),
                totalCoAuthorCount(id)
        );
    }

    private int totalCitedBy(String id) {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person { ID:$ID})-[:authorOf]->()<-[:references]-(citingDoc)  RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }

    private int totalCoAuthorCount(String id) {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person {ID:$ID})-[:authorOf]->()<-[:authorOf]-(co) RETURN COUNT(DISTINCT co) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }
//
//    public void hIndex (Neo4jClient neo4j){this.neo4j = neo4j;}
    private int totalHIndexCount(String id)
    {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person { ID:$ID})-[:authorOf]->(work) WITH work,SIZE((work)<-[:references]-()) AS citations ORDER BY citations RETURN org.elsevier.hIndex(COLLECT(citations)) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }

}


// QUESTION why do we need orderBy for hindex?
