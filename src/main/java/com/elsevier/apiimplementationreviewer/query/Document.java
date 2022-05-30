package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.Neo4jClient;
import org.neo4j.driver.Record;
import org.neo4j.util.concurrent.Work;

import java.util.Map;

public class Document {
    private Neo4jClient neo4j;

    public Document (Neo4jClient neo4j) {this.neo4j = neo4j;}
    public int totalDocCitedBy(String id)
    {
        Record docCount = this.neo4j.executeSingleQuery("MATCH (n:Work {ID:$ID}) RETURN SIZE((n)<-[:references]-()) AS result;", Map.of("ID", id)); //we need return size for a work
        return docCount.get("result").asInt();
    }
}




