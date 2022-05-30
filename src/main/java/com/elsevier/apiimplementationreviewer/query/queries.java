//package com.elsevier.citation.graph.api.queries;
//
//import com.elsevier.rs.graph.models.dao.*;
//
//public class CitationQueries {
//
//    private CitationQueries(){}
//
//    public static final String WORK_CITATIONS_COUNT = "MATCH (n:" + Work.TYPE + " {" + Work.PROPERTY_ID + ":$ID}) " +
//            "RETURN SIZE((n)<-[:" + Work.REL_REFERENCES + "]-()) AS result";
//
//    public static final String AUTHOR_CITATIONS_COUNT = "MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->()<-[:" + Work.REL_REFERENCES + "]-(citingDoc) " +
//            "RETURN COUNT(DISTINCT citingDoc) AS result";
//
//    public static final String CUMULATIVE_AUTHOR_CITATIONS = "MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(n) " +
//            "RETURN SUM(SIZE((n)<-[:" + Work.REL_REFERENCES + "]-())) AS result";
//
//    public static final String AUTHOR_PAPER_COUNT = "MATCH (a:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID}) " +
//            "RETURN SIZE((a)-[:" + Person.REL_AUTHOR_OF + "]->()) AS result";
//
//    public static final String AUTHOR_COAUTHORS_COUNT = "MATCH (me:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->()<-[:" + Person.REL_AUTHOR_OF + "]-(co) " +
//            "WHERE me." + Person.PROPERTY_ID + " <> co." + Person.PROPERTY_ID + " " +
//            "RETURN COUNT(DISTINCT co) AS result";
//
//    public static final String AUTHOR_HINDEX = "MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(work) " +
//            "WITH work, SIZE((work)<-[:" + Work.REL_REFERENCES + "]-()) AS citations " +
//            "ORDER BY citations " +
//            "RETURN org.elsevier.hIndex(COLLECT(citations)) AS result";
//
//    // These implementations are to enable like-for-like comparison during the transition to Group limited metrics
//    public static final String AUTHOR_HINDEX_SCE = "MATCH (me:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(work)<-[:" + Work.REL_REFERENCES + "]-(ref) " +
//            "WHERE NOT (me)-[:" + Person.REL_AUTHOR_OF + "]->(ref) " +
//            "WITH COUNT(ref) AS citations " +
//            "ORDER BY citations " +
//            "RETURN org.elsevier.hIndex(COLLECT(citations)) AS result";
//
//    public static final String AUTHOR_HINDEX_SCOPUS = "MATCH (:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(work)-[:"+ Work.REL_MEMBER_OF_GROUP +"]->(group)" +
//            "WHERE group." + Group.PROPERTY_NAME + " = \"" + Group.ALL_SCOPUS_CONTENT + "\" " +
//            "WITH work, SIZE((work)<-[:" + Work.REL_REFERENCES + "]-()) AS citations " +
//            "ORDER BY citations " +
//            "RETURN org.elsevier.hIndex(COLLECT(citations)) AS result";
//
//    public static final String AUTHOR_HINDEX_SCOPUS_SCE = "MATCH (me:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->(work)<-[:" + Work.REL_REFERENCES + "]-(ref) " +
//            "WHERE (:Group{"+ Group.PROPERTY_NAME +":\"" + Group.ALL_SCOPUS_CONTENT + "\"})<-[:"+ Work.REL_MEMBER_OF_GROUP +"]-(work) " +
//            "AND (:Group{"+ Group.PROPERTY_NAME +":\"" + Group.ALL_SCOPUS_CONTENT + "\"})<-[:"+ Work.REL_MEMBER_OF_GROUP +"]-(ref) " +
//            "AND NOT (me)-[:" + Person.REL_AUTHOR_OF + "]->(ref) " +
//            "WITH COUNT(ref) AS citations " +
//            "ORDER BY citations " +
//            "RETURN org.elsevier.hIndex(COLLECT(citations)) AS result";
//
//    // end
//
//    public static final String AUTHOR_COAUTHORS_LIST = "MATCH (me:" + Person.TYPE + " {" + Person.PROPERTY_ID + ":$ID})-[:" + Person.REL_AUTHOR_OF + "]->()<-[:" + Person.REL_AUTHOR_OF + "]-(co) " +
//            "WHERE me." + Person.PROPERTY_ID + " <> co." + Person.PROPERTY_ID + " " +
//            "RETURN COLLECT(DISTINCT co." + Person.PROPERTY_ID + ") AS result";
//
//    public static final String AFFILIATION_DOCUMENTS_COUNT = "MATCH (o:" + Organization.TYPE + " {" + Organization.PROPERTY_ID + ":$ID}) " +
//            "RETURN SIZE((o)-[:" + Organization.REL_ORIGINATED + "]->()) AS result";
//
//    public static final String AFFILIATION_DOCUMENTS_ROLLED_UP_COUNT = "MATCH (:" + Organization.TYPE + " {" + Organization.PROPERTY_ID + ":$ID})<-[:" + Organization.REL_CHILDOF + "*0..]-()-[:" + Organization.REL_ORIGINATED + "]->(work) " +
//            "RETURN COUNT(DISTINCT work) AS result";
//
//    public static final String WORK_CITED_BY_QUERY = "MATCH (n:" + Work.TYPE + " {" + Work.PROPERTY_ID + ":$ID})<-[:" + Work.REL_REFERENCES + "]-(citingDoc) " +
//            "RETURN COLLECT(citingDoc." + Work.PROPERTY_ID + ") AS result";
//
//    public static final String BATCH_WORK_CITED_BY_COUNTS = "MATCH (n:" + Work.TYPE + ") " +
//            "WHERE n." + Work.PROPERTY_ID + " IN $ID " +
//            "OPTIONAL MATCH (n)<-[:" + Work.REL_REFERENCES + "]-(doc) " +
//            "RETURN n." + Work.PROPERTY_ID + " AS nid, COUNT(doc) AS result";
//}

