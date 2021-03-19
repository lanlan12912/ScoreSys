package com.yelanlan.scoremanagersystem.RepositoryImpl.Common;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;

public class Message implements IMessage {
    private String code;//返回码
    private String msg;//返回消息
    private Object data;//返回数据
    private boolean success;//返回执行状态

    public Message() {
    }
    public Message(boolean success,String msg){
        this.success = success;
        this.msg = msg;
    }
    public  Message(boolean success,String msg,Object data){
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public Message(String code, String msg, Object data, boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
