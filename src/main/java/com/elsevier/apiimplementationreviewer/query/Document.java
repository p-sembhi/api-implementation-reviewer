package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.Neo4jClient;
import org.neo4j.driver.Record;
import org.neo4j.util.concurrent.Work;

import java.util.Map;

public class Document {
    private Neo4jClient neo4j;

    public Document (Neo4jClient neo4j) {this.neo4j = neo4j;}
    public int totalCitedBy(String id)
    {
        Record docCount = this.neo4j.executeSingleQuery("MATCH (:Work {ID:$ID})-[:references]->()<-[:refernces]-(citingDoc) RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID", id));
        return docCount.get("result").asInt();
    }
}


