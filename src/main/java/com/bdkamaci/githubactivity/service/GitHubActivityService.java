package com.bdkamaci.githubactivity.service;

import com.bdkamaci.githubactivity.dto.ActivityDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface GitHubActivityService {
    List<ActivityDTO> getActivity(String username, String eventType) throws JsonProcessingException;
}
