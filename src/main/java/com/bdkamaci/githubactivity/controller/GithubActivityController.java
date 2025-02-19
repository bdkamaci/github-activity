package com.bdkamaci.githubactivity.controller;

import com.bdkamaci.githubactivity.dto.ActivityDTO;
import com.bdkamaci.githubactivity.service.GitHubActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GithubActivityController {
    private final GitHubActivityService gitHubActivityService;

    @GetMapping("/activity/{username}")
    public List<ActivityDTO> getUserActivity(@PathVariable String username,
                                             @RequestParam(required = false) String eventType) throws JsonProcessingException {
        return gitHubActivityService.getActivity(username, eventType);
    }
}
