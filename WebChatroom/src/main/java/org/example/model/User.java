package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.base.JSONResponse;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class User extends JSONResponse implements Serializable {

    private Integer userId;

    private String name;

    private String password;

    private String nickName;

    private String iconPath;

    private String signature;

    private Date lastLogout;
}