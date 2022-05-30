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

    public void CoAuthor (Neo4jClient neo4j){
        this.neo4j = neo4j;
    }
    public int totalCoAuthorCount(String id)
    {
        Record count = this.neo4j.executeSingleQuery("MATCH (:Person {ID:$ID})-[:authorOf]->()<-[:authorOf]-(co) RETURN COUNT(DISTINCT co) AS result;", Map.of("ID", id));
        return count.get("result").asInt();
    }

    public hIndex (Neo4jClient neo4j){this.neo4j = neo4j;}
    public int totalHIndexCount(String id)
    {
        Record count = this.neo4j.executeSingleQuery("");
        return count.get("result").asInt();
    }
}
//
//"MATCH (me:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->()<-[:" + Person.REL_AUTHOR_OF + "]-(co) " +"WHERE me." + Person.PROPERTY_ID + " <> co." + Person.PROPERTY_ID + " " +"RETURN COUNT(DISTINCT co) AS result";
//"MATCH (n:Person {ID:$ID})-[:authorOf]->()<-[:authorOf]-(co) RETURN COUNT(DISTINCT co) AS result;", Map.of("ID", id));
//
//"MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->()<-[:" + Work.REL_REFERENCES + "]-(citingDoc) "RETURN COUNT(DISTINCT citingDoc) AS result"; "
//MATCH (:Person { ID:$ID})-[:authorOf]->()<-[:references]-(citingDoc)  RETURN COUNT(DISTINCT citingDoc) AS result;", Map.of("ID"


MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(work) " + "WITH work, SIZE((work)<-[:" + Work.REL_REFERENCES + "]-()) AS citations " +  "ORDER BY citations " + "RETURN org.elsevier.hIndex(COLLECT(citations)) AS result"; " +
"MATCH (:Person { ID:$ID})-[:authorOf]->(work)-RETURN SIZE(work) org.elsevier.hIndex(COLLECT(citations)) AS result;";
