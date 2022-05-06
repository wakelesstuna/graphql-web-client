# graphql-web-client

GraphQL client for consuming GraphQL APIs from a Spring Boot application.

![version](https://img.shields.io/badge/version-0.0.3-%23eb0195)
[![license](https://img.shields.io/github/license/DAVFoundation/captain-n3m0.svg?style=flat-square)](https://github.com/wakelesstuna/graphql-web-client/blob/main/LICENSE-MIT)


## Getting started

Add the starter to your project.

When using Maven:
```xml
<dependency>
    <groupId>io.wakelesstuna</groupId>
    <artifactId>graphql-web-client</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```
Configure your clients with the URL to the GraphQL APIs to consume.
```yaml
graphql:
  clients:
    userWebClient: http://localhost:8081/graphql
    postWebClient: http://localhost:8082/graphql
```
The package creates a map with instances of type `GraphQLWebClient` that you can use to create your beans with the key vaules in the map is the name, for example `userWebClient`. 
</br>An example of the config file:

```java
@Configuration
@RequiredArgsConstructor
public class GraphQLWebClientsConfig {

    private final GraphQLWebClientFactory factory;

    @Bean("userWebClient")
    public GraphQLWebClient userWebClient() {
        return factory.getWebClients().get("userWebClient");
    }

    @Bean("postWebClient")
    public GraphQLWebClient postWebClient() {
        return factory.getWebClients().get("postWebClient");
    }
}
```

If you want to override the default `ObejctMapper` you can create a bean with name `GraphQLObjectMapper`.
<br/>An simple example file:

```java
@Bean("GraphQLObjectMapper")
public ObjectMapper objectMapper() {
    ObjectMapper ob = new ObjectMapper();
    ob.registerModule(new JavaTimeModule());
    return ob;
}
```
