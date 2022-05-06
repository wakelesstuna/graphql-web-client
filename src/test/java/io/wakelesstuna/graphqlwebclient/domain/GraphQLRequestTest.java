package io.wakelesstuna.graphqlwebclient.domain;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class GraphQLRequestTest {

    /*@Test
    void testResourceLoader() {
        final String expectedQuery = "query{user{username}}";
        final String expectedJwt = "Bearer " + UUID.randomUUID();
        GraphQLRequest request = GraphQLRequest.builder()
                .header(AUTHORIZATION, expectedJwt)
                .loadQueryFromResource("graphqlrequest/userQuery.graphql")
                .operationName("testName")
                .variables(new String[]{"test", "test2"})
                .build();
        String query = request.getRequestBody().getQuery();
        request.getRequestBody().getOperationName();
        request.getRequestBody().getVariables();
        System.out.println(request.getRequestBody().toString());
        assertEquals(expectedQuery, query);
        assertEquals(expectedJwt, Objects.requireNonNull(request.getHttpHeaders().get(AUTHORIZATION)).get(0));
    }*/
}