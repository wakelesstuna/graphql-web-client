package io.wakelesstuna.graphqlwebclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "graphql")
public class GraphQLProperties {
    private Map<String, String> clients;
}
