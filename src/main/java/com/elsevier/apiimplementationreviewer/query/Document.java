package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.Neo4jClient;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import org.neo4j.driver.Record;

import java.util.Map;

//this class is responsible for running queries and returning the result set fo documents
public class Document implements QueryMetric {
    private Neo4jClient neo4j;

    public Document (Neo4jClient neo4j) {this.neo4j = neo4j;}
    public DocumentMetric getMetric(String id){
        return new DocumentMetric(
                id,
                totalDocCitedBy(id)
        );
    }
    private int totalDocCitedBy(String id)
    {
        Record docCount = this.neo4j.executeSingleQuery("MATCH (n:Work {ID:$ID}) RETURN SIZE((n)<-[:references]-()) AS result;", Map.of("ID", id)); //we need return size for a work
        return docCount.get("result").asInt();
    } //this just returns id of doc so do we want another query to collect citation count for a doc? update- no this
    // should return 333 in csv file

//    public int totalCitedByDoc(String id) {
//        Record count = this.neo4j.executeSingleQuery("MATCH (:work { ID:$ID})-[:work]->()<-[:references]-" +
//                "(citingDoc)  RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID", id));
//        return count.get("result").asInt();
//    }
}




