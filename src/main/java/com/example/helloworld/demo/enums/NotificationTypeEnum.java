package com.example.helloworld.demo.enums;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论"),
    LIKE_QUESTION(3, "赞了问题"),
    LIKE_COMMENT(4, "赞了评论");
    private Integer type;
    private String name;

    public static boolean isExist(Integer type) {
        for (NotificationTypeEnum value : NotificationTypeEnum.values()) {
            if (value.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static String getNameByType(Integer type){
        for (NotificationTypeEnum value : NotificationTypeEnum.values()) {
            if (value.getType() == type) {
                return value.name;
            }
        }
        return "";
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
