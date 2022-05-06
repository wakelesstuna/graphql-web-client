package io.wakelesstuna.graphqlwebclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.wakelesstuna.graphqlwebclient.GraphQLWebClient;
import io.wakelesstuna.graphqlwebclient.factory.GraphQLWebClientFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AutoConfigureAfter({JacksonAutoConfiguration.class, WebFluxAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLProperties.class)
public class GraphQLWebClientAutoConfiguration {

    private final GraphQLProperties graphQLProperties;
    private final ObjectMapper objectMapper;
    @Getter
    private final Map<String, GraphQLWebClient> webClients = new HashMap<>();

    public GraphQLWebClientAutoConfiguration(GraphQLProperties graphQLProperties, @Nullable @Qualifier("GraphQLObjectMapper") ObjectMapper objectMapper) {
        this.graphQLProperties = graphQLProperties;
        this.objectMapper = setObjectMapper(objectMapper);
        initClients();
    }

    public void initClients() {
        this.graphQLProperties.getClients()
                .forEach((beanName, baseUrl) -> {
                    log.info("Creating GraphQLWebClient with named: [{}] with base url: ({}) ", beanName, baseUrl);
                    webClients.put(beanName, GraphQLWebClientFactory.create(baseUrl, objectMapper));
                });
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
