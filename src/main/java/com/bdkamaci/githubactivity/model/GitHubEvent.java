package com.bdkamaci.githubactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubEvent {
    private String id;
    private String type;

    @JsonProperty("created_at")
    private String createdAt;

    private Actor actor;
    private Repo repo;
    private Payload payload;
}
