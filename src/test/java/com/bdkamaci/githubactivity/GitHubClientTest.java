package com.bdkamaci.githubactivity;

import com.bdkamaci.githubactivity.client.GitHubClient;
import com.bdkamaci.githubactivity.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubClientTest {
    private GitHubClient gitHubClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        // WebClient.Builder
        when(webClientBuilder.build()).thenReturn(webClient);
        gitHubClient = new GitHubClient(webClientBuilder);
        ReflectionTestUtils.setField(gitHubClient, "githubApiUrl", "https://api.github.com/users/");

        // WebClient chain
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // onStatus methods
        lenient().when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void getUserActivity_Success() {
        // Arrange
        String username = "testuser";
        String expectedResponse = "[{\"type\":\"PushEvent\"}]";

        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expectedResponse));

        // Act
        String result = gitHubClient.getUserActivity(username);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getUserActivity_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";

        // 4xx mock
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new UserNotFoundException("Invalid username: " + username)));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                gitHubClient.getUserActivity(username));
    }

    @Test
    void getUserActivity_ServerError() {
        // Arrange
        String username = "testuser";

        // 5xx mock
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new RuntimeException("Server error")));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                gitHubClient.getUserActivity(username));
    }
}
