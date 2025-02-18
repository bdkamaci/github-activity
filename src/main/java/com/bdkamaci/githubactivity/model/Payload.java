package com.bdkamaci.githubactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Payload {
    @JsonProperty("push_id")
    private Long pushId;
    private Integer size;
    @JsonProperty("distinct_size")
    private Integer distinctSize;
    @JsonProperty("ref")
    private String reference;
    private String head;
    private String before;
    private List<Commit> commits;
}
