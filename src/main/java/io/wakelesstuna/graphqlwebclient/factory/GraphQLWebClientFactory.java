package io.wakelesstuna.graphqlwebclient.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.wakelesstuna.graphqlwebclient.GraphQLWebClient;
import io.wakelesstuna.graphqlwebclient.config.GraphQLProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GraphQLWebClientFactory {

    private final GraphQLProperties graphQLProperties;
    private final ObjectMapper objectMapper;
    @Getter
    private final Map<String, GraphQLWebClient> webClients = new HashMap<>();

    public GraphQLWebClientFactory(GraphQLProperties graphQLProperties, @Nullable @Qualifier("GraphQLObjectMapper") ObjectMapper objectMapper) {
        this.graphQLProperties = graphQLProperties;
        this.objectMapper = setObjectMapper(objectMapper);
        createClients();
    }

    public void createClients() {
        this.graphQLProperties.getClients()
                .forEach((beanName, baseUrl) -> {
                    log.info("Creating GraphQLWebClient with named: [{}] with base url: ({}) ", beanName, baseUrl);
                    webClients.put(beanName, GraphQLWebClientFactory.create(baseUrl, objectMapper));
                });
    }

    private static GraphQLWebClient create(String url, ObjectMapper ob) {
        WebClient webClient = WebClient.create(url);
        return new GraphQLWebClient(webClient, ob);
    }

    private ObjectMapper setObjectMapper(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            log.info("Using default object mapper when creating GraphQLWebClients");
            return new ObjectMapper();
        } else {
            log.info("Using custom object mapper when creating GraphQLWebClients");
            return objectMapper;
        }
    }
}
