package com.elsevier.apiimplementationreviewer.query;

import com.elsevier.apiimplementationreviewer.helper.Neo4jClient;
import com.elsevier.apiimplementationreviewer.metrics.DocumentMetric;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//this class is responsible for running queries and returning the result set fo documents
public class Document implements QueryMetric {
    private final Neo4jClient neo4j;

    public Document (Neo4jClient neo4j) {this.neo4j = neo4j;}
    public DocumentMetric getMetric(String id){
        return new DocumentMetric(
                id,
                totalDocCitedBy(id),
                citedByIds(id)
        );
    }
    private int totalDocCitedBy(String id)
    {
        try {
            Record docCount = this.neo4j.executeSingleQuery("MATCH (n:Work {ID:$ID}) RETURN SIZE((n)<-[:references]-()) AS result;", Map.of("ID", id)); //we need return size for a work
            return docCount.get("result").asInt();
        } catch (NoSuchRecordException exception) {
            return 0;
        }
    } //this just returns id of doc so do we want another query to collect citation count for a doc? update- no this
    // should return 333 in csv file

    private List<String> citedByIds(String id) {
        try {
            Record record = this.neo4j.executeSingleQuery("MATCH (n:Work {ID:$ID})<-[:references]-(citingDoc) RETURN COLLECT(citingDoc.ID) AS result", Map.of("ID", id)); //we need return size for a work
            return record.get("result").asList().stream().map(Object::toString).collect(Collectors.toList());
        } catch (NoSuchRecordException exception) {
            return Collections.emptyList();
        }
    }
}




