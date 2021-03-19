package com.yelanlan.scoremanagersystem.Repository_Iface.Common;

public interface IMessage {
    public String getCode();
    public void setCode(String code);
    public String getMsg();
    public void setMsg(String msg);
    public Object getData();
    public void setData(Object data);
    public boolean isSuccess();
    public void setSuccess(boolean success);
}
