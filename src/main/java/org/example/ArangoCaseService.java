package org.example;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoCursor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ArangoCaseService {
    private static final Logger logger = Logger.getLogger(ArangoCaseService.class.getName());

    public List<Case> getAllCases() {
        List<Case> cases = new ArrayList<>();
        try {
            ArangoDB arangoDB = new ArangoDB.Builder()
                    .host("localhost", 8529)
                    .user("root") // change if you use a different user
                    .password("root") // change if you use a password
                    .build();
            var database = arangoDB.db("source");
            String query = "FOR doc IN case_entity RETURN doc";
            try (ArangoCursor<Case> cursor = database.query(query, Case.class)) {
                cursor.forEachRemaining(cases::add);
            }
        } catch (Exception e) {
            logger.severe("Error connecting to ArangoDB or fetching cases: " + e.getMessage());
        }
        return cases;
    }
}
