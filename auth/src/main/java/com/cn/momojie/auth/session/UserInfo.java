package com.cn.momojie.auth.session;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class UserInfo {

    private Boolean login = false;

    private String name = "";

    private Long id;

    private String moTalkOpenId;

    private List<String> roles = new LinkedList<>();

}
