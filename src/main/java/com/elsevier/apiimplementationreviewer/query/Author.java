package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.Neo4jClient;
import org.neo4j.driver.Record;

import java.util.List;
import java.util.Map;

public class Author {
    private Neo4jClient neo4j;

    public Author (Neo4jClient neo4j){
       this.neo4j = neo4j;
    }
    public int totalCitedBy(String id )
    {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person { ID:$ID})-[:authorOf]->()<-[:references]-(citingDoc)  RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }
}
