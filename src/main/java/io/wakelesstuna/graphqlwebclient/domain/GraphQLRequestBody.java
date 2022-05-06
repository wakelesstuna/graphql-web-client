package io.wakelesstuna.graphqlwebclient.domain;

import lombok.Value;

@Value
public class GraphQLRequestBody {
    private final String query;
    private final Object variables;
    private final String operationName;

    private GraphQLRequestBody(String query, Object variables, String operationName) {
        this.query = query;
        this.variables = variables;
        this.operationName = operationName;
    }

    public static GraphQLRequestBodyBuilder builder() {
        return new GraphQLRequestBodyBuilder();
    }

    public static class GraphQLRequestBodyBuilder {
        private String query;
        private Object variables;
        private String operationName;

        GraphQLRequestBodyBuilder() {
        }

        public GraphQLRequestBodyBuilder query(String query) {
            this.query = query;
            return this;
        }

        public GraphQLRequestBodyBuilder variables(Object variables) {
            this.variables = variables;
            return this;
        }

        public GraphQLRequestBodyBuilder operationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public GraphQLRequestBody build() {
            return new GraphQLRequestBody(query, variables, operationName);
        }

        public String toString() {
            return "GraphQLRequestBody.GraphQLRequestBodyBuilder(query=" + this.query + ", variables=" + this.variables + ", operationName=" + this.operationName + ")";
        }
    }
}
