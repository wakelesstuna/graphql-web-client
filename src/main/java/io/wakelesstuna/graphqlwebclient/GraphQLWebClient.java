package io.wakelesstuna.graphqlwebclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wakelesstuna.graphqlwebclient.domain.GraphQLRequest;
import io.wakelesstuna.graphqlwebclient.domain.GraphQLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class GraphQLWebClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public Mono<GraphQLResponse> post(GraphQLRequest request) {
        WebClient.RequestBodySpec spec = webClient.post().contentType(APPLICATION_JSON);
        request.getHttpHeaders()
                .forEach((header, values) -> spec.header(header, values.toArray(new String[0])));
        return spec.bodyValue(request.getRequestBody())
                .retrieve()
                .bodyToMono(String.class)
                .map(it -> new GraphQLResponse(it, objectMapper));
    }
}
