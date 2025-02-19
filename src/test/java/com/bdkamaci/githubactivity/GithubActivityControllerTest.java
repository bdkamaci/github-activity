package com.bdkamaci.githubactivity;

import com.bdkamaci.githubactivity.controller.GithubActivityController;
import com.bdkamaci.githubactivity.dto.ActivityDTO;
import com.bdkamaci.githubactivity.service.GitHubActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubActivityControllerTest {
    @Mock
    private GitHubActivityService gitHubActivityService;

    @InjectMocks
    private GithubActivityController githubActivityController;

    @Test
    void getUserActivity_WithEventType_ShouldReturnFilteredActivities() throws JsonProcessingException {
        // Arrange
        String username = "testuser";
        String eventType = "PushEvent";
        List<ActivityDTO> expectedActivities = List.of(
                new ActivityDTO("PushEvent", "repo1", "2024-02-19T10:00:00Z")
        );

        when(gitHubActivityService.getActivity(username, eventType))
                .thenReturn(expectedActivities);

        // Act
        List<ActivityDTO> result = githubActivityController.getUserActivity(username, eventType);

        // Assert
        assertNotNull(result);
        assertEquals(expectedActivities.size(), result.size());
        assertEquals(expectedActivities.get(0), result.get(0));
        verify(gitHubActivityService).getActivity(username, eventType);
    }

    @Test
    void getUserActivity_WithoutEventType_ShouldReturnAllActivities() throws JsonProcessingException {
        // Arrange
        String username = "testuser";
        List<ActivityDTO> expectedActivities = List.of(
                new ActivityDTO("PushEvent", "repo1", "2024-02-19T10:00:00Z"),
                new ActivityDTO("CreateEvent", "repo2", "2024-02-19T11:00:00Z")
        );

        when(gitHubActivityService.getActivity(username, null))
                .thenReturn(expectedActivities);

        // Act
        List<ActivityDTO> result = githubActivityController.getUserActivity(username, null);

        // Assert
        assertNotNull(result);
        assertEquals(expectedActivities.size(), result.size());
        verify(gitHubActivityService).getActivity(username, null);
    }

    @Test
    void getUserActivity_WhenJsonProcessingExceptionOccurs_ShouldThrowException() throws JsonProcessingException {
        // Arrange
        String username = "testuser";
        when(gitHubActivityService.getActivity(anyString(), any()))
                .thenThrow(new JsonProcessingException("Test error") {});

        // Act & Assert
        assertThrows(JsonProcessingException.class, () ->
                githubActivityController.getUserActivity(username, null));
    }
}