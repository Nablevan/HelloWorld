package com.example.helloworld.demo.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不存在"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NOT_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004, "服务器：亡了亡了，我没有史莱姆亡了"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "你回复的评论不存在"),
    NOTIFICATION_NOT_FOUND(2007, "你查看的通知不存在"),
    NOTIFICATION_RECEIVER_ERROR(2008, "你还想看别人的通知？");

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
