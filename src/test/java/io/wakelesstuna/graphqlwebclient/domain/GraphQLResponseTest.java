package io.wakelesstuna.graphqlwebclient.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.wakelesstuna.graphqlwebclient.exception.GraphQLClientException;
import io.wakelesstuna.graphqlwebclient.exception.GraphQLErrorsException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GraphQLResponseTest {

    private static ObjectMapper ob;

    @BeforeAll
    static void beforeAll() {
        ob = new ObjectMapper();
        ob.registerModule(new JavaTimeModule());
    }

    @Test
    void testNestedObject() {
        final String expectedAuthor = "Test user";
        final int expectedCommentListSize = 2;

        String postResponse = readGraphQLFile("graphql-post-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(postResponse, ob);
        Post response = graphQLResponse.get("post", Post.class);

        assertNotNull(response);
        assertEquals(expectedAuthor, response.author.username);
        assertEquals(expectedCommentListSize, response.comments.size());
    }

    @Test
    void testListObject() {
        final String expectedAuthorOne = "Comment author 1";
        final int expectedCommentListSize = 2;

        String listUserResponse = readGraphQLFile("graphql-users-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(listUserResponse, ob);

        List<User> response = graphQLResponse.getList("users", User.class);

        assertNotNull(response);
        assertEquals(expectedCommentListSize, response.size());
        assertEquals(expectedAuthorOne, response.get(0).username);
    }

    @Test
    void testGetFirstPost() {
        final String expectedPostTitle = "This is a Post object for testing";

        String listUserResponse = readGraphQLFile("graphql-post-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(listUserResponse, ob);

        Post response = graphQLResponse.getFirst(Post.class);

        assertNotNull(response);
        assertEquals(expectedPostTitle, response.title);
    }

    @Test
    void testGetFirstList() {
        final String expectedAuthorOne = "Comment author 1";
        final int expectedCommentListSize = 2;

        String listUserResponse = readGraphQLFile("graphql-users-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(listUserResponse, ob);

        List<User> response = graphQLResponse.getFirstList(User.class);

        assertNotNull(response);
        assertEquals(expectedCommentListSize, response.size());
        assertEquals(expectedAuthorOne, response.get(0).username);
    }

    @Test
    void notValidJsonResponse() {
        String notValidResponse = readGraphQLFile("not-valid-response.json");
        assertThrows(GraphQLClientException.class,() -> new GraphQLResponse(notValidResponse, ob));
    }

    @Test
    void readErrorResponse() {
        final int expectedErrorSize = 1;
        final String expectedErrorMessage = "Validation error of type SubSelectionRequired: Sub selection required for type Message! of field post @ 'post'";
        String errorResponse = readGraphQLFile("error-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        assertEquals(expectedErrorSize, graphQLResponse.getErrors().size());
        assertEquals(expectedErrorMessage, graphQLResponse.getErrors().get(0).getMessage());
    }

    @Test
    void validateErrors() {
        final int expectedErrorSize = 1;
        final String expectedErrorMessage = "Validation error of type SubSelectionRequired: Sub selection required for type Message! of field post @ 'post'";
        String errorResponse = readGraphQLFile("error-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        assertThrows(GraphQLErrorsException.class, graphQLResponse::validateNoErrors);

        assertEquals(expectedErrorSize, graphQLResponse.getErrors().size());
        assertEquals(expectedErrorMessage, graphQLResponse.getErrors().get(0).getMessage());
    }

    @Test
    void testEmptyDataResponse() {
        String errorResponse = readGraphQLFile("empty-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        User first = graphQLResponse.get("user",User.class);
        assertNull(first);
    }

    @Test
    void testEmptyDataResponseFirstEntry() {
        String errorResponse = readGraphQLFile("empty-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        User first = graphQLResponse.getFirst(User.class);
        assertNull(first);
    }

    @Test
    void testEmptyListResponse() {
        String errorResponse = readGraphQLFile("empty-list-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        assertTrue(graphQLResponse.getList("post",User.class).isEmpty());
    }

    @Test
    void testGetFieldThatDontExists() {
        String postResponse = readGraphQLFile("graphql-post-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(postResponse, ob);
        Post post = graphQLResponse.get("user", Post.class);
        assertNull(post);
    }

    @Test
    void testErrorsStructure() {
        final String expectedErrorMessage = "Validation error of type SubSelectionRequired: Sub selection required for type Message! of field post @ 'post'";
        final int expectedLocationSize = 1;
        final String expectedClassification= "ValidationError";
        final int expectedLocationLine = 2;
        final int expectedLocationColumn = 3;
        final int expectedPathSize = 2;
        final String expectedFirstPath = "path1";

        String errorResponse = readGraphQLFile("error-response.json");
        GraphQLResponse graphQLResponse = new GraphQLResponse(errorResponse, ob);
        GraphQLError error = graphQLResponse.getErrors().get(0);

        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(expectedPathSize, error.getPath().size());
        assertEquals(expectedFirstPath, error.getPath().get(0));
        assertEquals(expectedLocationSize, error.getLocations().size());
        assertEquals(expectedLocationLine, error.getLocations().get(0).getLine());
        assertEquals(expectedLocationColumn, error.getLocations().get(0).getColumn());
        assertEquals(expectedClassification, error.getExtensions().get("classification"));
    }

    public String readGraphQLFile(String filename) {
        try {
            File myObj = new File("./src/test/resources/graphqlresponse", filename);
            Scanner myReader = new Scanner(myObj);
            StringBuilder response = new StringBuilder();
            while (myReader.hasNextLine()) {
                response.append(myReader.nextLine());
            }
            myReader.close();
            return String.valueOf(response);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    @NoArgsConstructor
    @Data
    static class Post {
        String title;
        Integer numberOfComments;
        User author;
        LocalDateTime createdAt;
        List<Comment> comments;
    }

    @NoArgsConstructor
    @Data
    static class User {
        String username;
    }

    @NoArgsConstructor
    @Data
    static class Comment {
        User user;
        String comment;
    }

}