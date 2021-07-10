package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.base.JSONResponse;

@Getter
@Setter
@ToString
public class Channel extends JSONResponse {
    private Integer channelId;

    private String channelName;
}