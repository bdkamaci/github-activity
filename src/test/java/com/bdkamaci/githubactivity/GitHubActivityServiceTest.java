package com.bdkamaci.githubactivity;

import com.bdkamaci.githubactivity.client.GitHubClient;
import com.bdkamaci.githubactivity.dto.ActivityDTO;
import com.bdkamaci.githubactivity.model.GitHubEvent;
import com.bdkamaci.githubactivity.model.Repo;
import com.bdkamaci.githubactivity.service.impl.GitHubActivityServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubActivityServiceTest {
    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GitHubActivityServiceImpl gitHubActivityService;

    private static final String USERNAME = "testUser";
    private static final String EVENT_TYPE = "PushEvent";

    private GitHubEvent testEvent;
    private String testJsonResponse;

    @BeforeEach
    void setUp() {
        // Test event creation
        testEvent = new GitHubEvent();
        testEvent.setId("1234567890");
        testEvent.setType(EVENT_TYPE);
        testEvent.setCreatedAt("2024-02-19T10:00:00Z");

        Repo repo = new Repo();
        repo.setName("test-repo");
        testEvent.setRepo(repo);

        // Test JSON response
        testJsonResponse = "[{\"id\":\"1234567890\",\"type\":\"PushEvent\",\"created_at\":\"2024-02-19T10:00:00Z\",\"repo\":{\"name\":\"test-repo\"}}]";
    }

    @Test
    void getActivity_WithEventType_ShouldFilterAndReturnMatchingEvents() throws JsonProcessingException {
        // Arrange
        List<GitHubEvent> mockEvents = List.of(testEvent);

        when(gitHubClient.getUserActivity(USERNAME)).thenReturn(testJsonResponse);
        when(objectMapper.readValue(eq(testJsonResponse), any(TypeReference.class))).thenReturn(mockEvents);

        // Act
        List<ActivityDTO> result = gitHubActivityService.getActivity(USERNAME, EVENT_TYPE);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(EVENT_TYPE, result.getFirst().getEventType());
        assertEquals("test-repo", result.getFirst().getRepoName());
        assertEquals("2024-02-19T10:00:00Z", result.getFirst().getEventTime());
    }

    @Test
    void getActivity_WithoutEventType_ShouldReturnAllEvents() throws JsonProcessingException {
        // Arrange
        GitHubEvent createEvent = new GitHubEvent();
        createEvent.setId("0987654321");
        createEvent.setType("CreateEvent");
        createEvent.setCreatedAt("2024-02-19T11:00:00Z");

        Repo createRepo = new Repo();
        createRepo.setName("another-repo");
        createEvent.setRepo(createRepo);

        List<GitHubEvent> mockEvents = List.of(testEvent, createEvent);
        String jsonResponseWithMultipleEvents = "[" +
                "{\"id\":\"1234567890\",\"type\":\"PushEvent\",\"created_at\":\"2024-02-19T10:00:00Z\",\"repo\":{\"name\":\"test-repo\"}}," +
                "{\"id\":\"0987654321\",\"type\":\"CreateEvent\",\"created_at\":\"2024-02-19T11:00:00Z\",\"repo\":{\"name\":\"another-repo\"}}" +
                "]";

        when(gitHubClient.getUserActivity(USERNAME)).thenReturn(jsonResponseWithMultipleEvents);
        when(objectMapper.readValue(eq(jsonResponseWithMultipleEvents), any(TypeReference.class))).thenReturn(mockEvents);

        // Act
        List<ActivityDTO> result = gitHubActivityService.getActivity(USERNAME, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PushEvent", result.get(0).getEventType());
        assertEquals("CreateEvent", result.get(1).getEventType());
    }

    @Test
    void getActivity_WhenJsonProcessingFails_ShouldPropagateException() throws JsonProcessingException {
        // Arrange
        when(gitHubClient.getUserActivity(USERNAME)).thenReturn(testJsonResponse);
        when(objectMapper.readValue(eq(testJsonResponse), any(TypeReference.class)))
                .thenThrow(new JsonProcessingException("Test error") {});

        // Act & Assert
        assertThrows(JsonProcessingException.class, () ->
                gitHubActivityService.getActivity(USERNAME, EVENT_TYPE));
    }
}
