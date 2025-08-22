package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/graphql/columns")
public class GraphQLColumnsController {
    @GetMapping
    public ResponseEntity<List<String>> getGraphQLColumns() {
        String graphqlEndpoint = "http://localhost:8081/graphql"; // Updated endpoint
        String inputTypeName = "CaseInput"; // Updated to match schema
        String introspectionQuery = "query IntrospectInputType {\n" +
                "  __type(name: \"" + inputTypeName + "\") {\n" +
                "    name\n" +
                "    inputFields {\n" +
                "      name\n" +
                "      type { name kind }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String safeQuery = introspectionQuery.replace("\n", " ").replace("\r", " ").replace("\"", "\\\"");
        String body = String.format("{\"query\": \"%s\"}", safeQuery);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(graphqlEndpoint, entity, Map.class);
        List<String> columns = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data == null) {
                System.out.println("No 'data' in GraphQL response: " + response.getBody());
                return ResponseEntity.ok(columns);
            }
            Map<String, Object> type = (Map<String, Object>) data.get("__type");
            if (type == null) {
                System.out.println("No '__type' in GraphQL response: " + response.getBody());
                return ResponseEntity.ok(columns);
            }
            List<Map<String, Object>> inputFields = (List<Map<String, Object>>) type.get("inputFields");
            if (inputFields == null) {
                System.out.println("No 'inputFields' in GraphQL response: " + response.getBody());
                return ResponseEntity.ok(columns);
            }
            for (Map<String, Object> field : inputFields) {
                columns.add((String) field.get("name"));
            }
        } catch (Exception e) {
            System.out.println("Error parsing GraphQL response: " + response.getBody());
            e.printStackTrace();
        }
        return ResponseEntity.ok(columns);
    }
}
