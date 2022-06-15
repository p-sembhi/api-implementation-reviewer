package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.Neo4jClient;
import org.neo4j.driver.Record;

import java.util.Map;

public class Author {
    private Neo4jClient neo4j;
    //this class is responsible for running queries and returning the result set

    public Author(Neo4jClient neo4j) {
        this.neo4j = neo4j;
    }


    public int totalCitedBy(String id) {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person { ID:$ID})-[:authorOf]->()<-[:references]-(citingDoc)  RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }

    public int totalCoAuthorCount(String id) {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person {ID:$ID})-[:authorOf]->()<-[:authorOf]-(co) RETURN COUNT(DISTINCT co) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }
//
//    public void hIndex (Neo4jClient neo4j){this.neo4j = neo4j;}
    public int totalHIndexCount(String id)
    {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person { ID:$ID})-[:authorOf]->(work) WITH work,SIZE((work)<-[:references]-()) AS citations ORDER BY citations RETURN org.elsevier.hIndex(COLLECT(citations)) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }

}


// QUESTION why do we need orderBy for hindex?
