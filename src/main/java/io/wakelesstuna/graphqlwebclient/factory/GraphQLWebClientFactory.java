package io.wakelesstuna.graphqlwebclient.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.graphqlwebclient.GraphQLWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class GraphQLWebClientFactory {

    private GraphQLWebClientFactory() {}

    public static GraphQLWebClient create(String url, ObjectMapper ob) {
        WebClient webClient = WebClient.create(url);
        return new GraphQLWebClient(webClient, ob);
    }
}
