package com.cn.momojie.basic.vo;

import com.cn.momojie.basic.constant.MsgCode;

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
