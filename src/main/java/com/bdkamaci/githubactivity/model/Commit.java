package com.bdkamaci.githubactivity.model;

import lombok.Data;

@Data
public class Commit {
    private String sha;
    private CommitAuthor author;
    private String message;
    private Boolean distinct;
    private String url;
}
