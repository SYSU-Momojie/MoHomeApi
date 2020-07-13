package com.cn.momojie.blog.vo;

import com.cn.momojie.blog.constant.MsgCode;
import lombok.Data;

@Data
public class MessageResult<T> {

    public MessageResult() {
        code = MsgCode.SUCCESS;
    }

    private Integer code;

    private String message;

    private T data;

    public void setFail(String msg) {
        code = MsgCode.FAIL;
        message = msg;
    }
}
