package io.wakelesstuna.graphqlwebclient.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.graphqlwebclient.exception.GraphQLClientException;
import io.wakelesstuna.graphqlwebclient.exception.GraphQLErrorsException;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class GraphQLResponse {

    public static final String ERRORS_FIELD = "errors";

    private final JsonNode data;
    @Getter
    private final List<GraphQLError> errors;
    private final ObjectMapper objectMapper;

    public GraphQLResponse(String rawResponse, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        JsonNode tree = readTree(rawResponse);
        errors = readErrors(tree);
        data = tree.get("data");
    }

    private JsonNode readTree(String rawResponse) {
        try {
            return objectMapper.readTree(rawResponse);
        } catch (JsonProcessingException e) {
            throw new GraphQLClientException("Cannot read response '" + rawResponse + "'", e);
        }
    }

    private List<GraphQLError> readErrors(JsonNode tree) {
        if (tree.hasNonNull(ERRORS_FIELD)) {
            return convertList(tree.get(ERRORS_FIELD), GraphQLError.class);
        }
        return emptyList();
    }

    private <T> List<T> convertList(JsonNode node, Class<T> type) {
        return objectMapper.convertValue(node, constructListType(type));
    }

    private JavaType constructListType(Class<?> type) {
        return objectMapper.getTypeFactory().constructCollectionType(List.class, type);
    }

    public <T> T get(String fieldName, Class<T> type) {
        if (data.hasNonNull(fieldName)) {
            return objectMapper.convertValue(data.get(fieldName), type);
        }
        return null;
    }

    public <T> T getFirst(Class<T> type) {
        return getFirstDataEntry().map(it -> objectMapper.convertValue(it, type)).orElse(null);
    }

    private Optional<JsonNode> getFirstDataEntry() {
        if (!data.isEmpty()) {
            return Optional.ofNullable(data.fields().next().getValue());
        }
        return Optional.empty();
    }

    public <T> List<T> getList(String fieldName, Class<T> type) {
        if (data.hasNonNull(fieldName)) {
            return convertList(data.get(fieldName), type);
        }
        return emptyList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getFirstList(Class<T> type) {
        return getFirstDataEntry()
                .map(it -> convertList(it, type))
                .map(List.class::cast)
                .orElseGet(Collections::emptyList);
    }

    public void validateNoErrors() {
        if (!errors.isEmpty()) {
            throw new GraphQLErrorsException(errors);
        }
    }
}
