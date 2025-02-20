package com.bdkamaci.githubactivity.service.impl;

import com.bdkamaci.githubactivity.client.GitHubClient;
import com.bdkamaci.githubactivity.dto.ActivityDTO;
import com.bdkamaci.githubactivity.model.GitHubEvent;
import com.bdkamaci.githubactivity.service.GitHubActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubActivityServiceImpl implements GitHubActivityService {
    private final GitHubClient gitHubClient;
    private final ObjectMapper objectMapper;


    @Override
    @Cacheable(cacheNames = "activities", key = "#username + ':' + #eventType")
    public List<ActivityDTO> getActivity(String username, String eventType) throws JsonProcessingException {
        List<GitHubEvent> events = getActivityFromGitHub(username);
        if (events == null) {
            return List.of();
        }
        return events.stream()
                .filter(event -> eventType == null || eventType.isEmpty() ||
                        event.getType().equalsIgnoreCase(eventType))
                .map(this::convertToDTO)
                .toList();
    }

    private List<GitHubEvent> getActivityFromGitHub(String username) throws JsonProcessingException {
        String jsonResponse = gitHubClient.getUserActivity(username);
        return objectMapper.readValue(jsonResponse, new TypeReference<List<GitHubEvent>>() {});
    }

    private ActivityDTO convertToDTO(GitHubEvent event) {
        return new ActivityDTO(
                event.getType(),
                event.getRepo().getName(),
                event.getCreatedAt()
        );
    }
}
