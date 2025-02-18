package com.bdkamaci.githubactivity.client;

import com.bdkamaci.githubactivity.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubClient {
    private final WebClient webClient;

    @Value("${github.api.url}")
    private String githubApiUrl;

    public GitHubClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getUserActivity(String username) {
        String url = githubApiUrl + username + "/events";

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new UserNotFoundException("Invalid username: " + username))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("An error occurred on the GitHub API server."))
                )
                .bodyToMono(String.class)
                .block();
    }
}
