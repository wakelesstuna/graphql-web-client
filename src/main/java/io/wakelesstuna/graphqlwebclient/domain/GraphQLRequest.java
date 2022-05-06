package io.wakelesstuna.graphqlwebclient.domain;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Getter
public class GraphQLRequest {

    private final HttpHeaders httpHeaders;
    private final GraphQLRequestBody requestBody;

    public GraphQLRequest(HttpHeaders headers, GraphQLRequestBody request) {
        this.httpHeaders = headers;
        this.requestBody = request;
    }

    public static GraphQLRequestBuilder builder() {
        return new GraphQLRequestBuilder();
    }

    public static class GraphQLRequestBuilder {

        private final HttpHeaders headers = new HttpHeaders();
        private final GraphQLRequestBody.GraphQLRequestBodyBuilder bodyBuilder = GraphQLRequestBody.builder();

        private GraphQLRequestBuilder() {
        }

        public GraphQLRequestBuilder header(String name, String... values) {
            headers.addAll(name, Arrays.asList(values));
            return this;
        }

        public GraphQLRequestBuilder loadQueryFromResource(String resource) {
            return query(loadQuery(resource));
        }

        @SneakyThrows
        private String loadQuery(String path) {
            return loadResource(new ClassPathResource(path));
        }

        private String loadResource(Resource resource) throws IOException {
            try (InputStream inputStream = resource.getInputStream()) {
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).replaceAll("[\n\r\s]", "");
            }
        }

        public GraphQLRequestBuilder query(String query) {
            bodyBuilder.query(query);
            return this;
        }

        public GraphQLRequestBuilder variables(Object variables) {
            bodyBuilder.variables(variables);
            return this;
        }

        public GraphQLRequestBuilder operationName(String operationName) {
            bodyBuilder.operationName(operationName);
            return this;
        }

        public GraphQLRequest build() {
            return new GraphQLRequest(headers, bodyBuilder.build());
        }

    }
}
