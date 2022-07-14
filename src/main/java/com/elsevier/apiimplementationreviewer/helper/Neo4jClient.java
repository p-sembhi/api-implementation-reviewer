package com.elsevier.apiimplementationreviewer;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.exceptions.Neo4jException;

import java.util.*;
import java.util.function.UnaryOperator;

//responsible for setting up connection to neo4j
public class Neo4jClient implements AutoCloseable {

//    private static final Logger logger = LoggerFactory.getLogger(Neo4jClient.class);
    private final Driver driver;
    private final String neo4jUrl;
    private final String database;




    public Neo4jClient(String url, String user, String password, boolean encryption, String database ) {
//        logger.info("url = " + url + " user = " + user + " database = " + );
        neo4jUrl = url;
        AuthToken authToken = AuthTokens.basic(user, password);

        Config config = Config.builder().withoutEncryption().build(); //builds the config for the neo4j object
        if (encryption) {
            config = Config.builder().withEncryption().build();
        }

        driver = GraphDatabase.driver(neo4jUrl, authToken, config);
        this.database = database;

    }


    public List<Record> executeQuery(final String query, Map<String, Object> parameters) throws Neo4jException {
            return getSession(AccessMode.READ).run(query,parameters).list();
        }

    public Record executeSingleQuery(final String query, Map<String, Object> parameters) throws Neo4jException {
        return getSession(AccessMode.READ).run(query,parameters).single();
    }


    @Override
    public void close() {
        driver.close();
    }


    public Session getSession(AccessMode accessMode) {
        UnaryOperator<SessionConfig.Builder> dbBuilder = (this.database == null || "".equals(this.database) ?
                (SessionConfig.Builder builder) -> builder :
                (SessionConfig.Builder builder) -> builder.withDatabase(this.database));
        SessionConfig builder = dbBuilder.apply(SessionConfig.builder().withDefaultAccessMode(accessMode)).build();

        return this.driver.session(builder);
    }

}
