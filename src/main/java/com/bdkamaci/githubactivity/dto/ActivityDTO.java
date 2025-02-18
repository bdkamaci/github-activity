package com.bdkamaci.githubactivity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {
    private String eventType;

    private String repoName;

    private String eventTime;
}
