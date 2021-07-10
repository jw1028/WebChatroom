package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Message {
    private Integer messageId;

    private Integer userId;

    private Integer channelId;

    private String content;

    private Date sendTime;

    private String nickName;
}