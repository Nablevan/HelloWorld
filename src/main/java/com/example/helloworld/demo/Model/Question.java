package com.example.helloworld.demo.Model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String title;
    private String tag;
    private String description;
    private Long gmt_create;
    private Long gmt_modified;
    private Integer creator;
    private Integer comment_count;
    private Integer view_count;
    private Integer like_count;
}
