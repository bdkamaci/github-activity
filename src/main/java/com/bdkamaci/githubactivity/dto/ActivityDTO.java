package com.bdkamaci.githubactivity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventType;
    private String repoName;
    private String eventTime;
}