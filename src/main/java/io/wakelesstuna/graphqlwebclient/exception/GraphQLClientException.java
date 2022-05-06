package io.wakelesstuna.graphqlwebclient.exception;

public class GraphQLClientException extends RuntimeException {
    public GraphQLClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
