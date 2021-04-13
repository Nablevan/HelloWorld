package com.example.helloworld.demo.dto;

import com.example.helloworld.demo.Model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outerTittle;
    private String type;
}
