package io.wakelesstuna.graphqlwebclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@AutoConfigureAfter({JacksonAutoConfiguration.class, WebFluxAutoConfiguration.class})
@EnableConfigurationProperties(GraphQLProperties.class)
public class GraphQLWebClientAutoConfiguration {

}
