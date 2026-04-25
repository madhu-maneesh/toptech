package com.toptech.toptech.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsModel {
    public String title;
    public String url;
    public int score;
}
