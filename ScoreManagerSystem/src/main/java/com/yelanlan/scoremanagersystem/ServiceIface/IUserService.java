package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

import java.util.List;
import java.util.Map;

public interface IUserService {
    /**
     * 根据用户账号查找用户信息
     * @param userNumber
     * @return
     * */
    public User findUserByNumber(String userNumber);

    /**
     * 验证登录密码是否正确
     * @param  userNumber
     * @param  userPwd
     * @param  isModify
     * @return
     * */
    public User identifyLogin(String userNumber, String userPwd,boolean isModify);

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * @param map
     * @return
     * */
    public boolean createUser(Map<String,String> map);

    /**
     *修改密码
     * @param userNumber
     * @param oldPwd
     * @param newPwd
     * @return
     * */
    public Message modifyPwd(String userNumber, String oldPwd, String newPwd);

    /**
     * 保存当前登陆的用户
     * @param  user
     * @return
     * */
    public void setCurrentuser(User user);

    /**
     * 获取当前登录的用户
     * @return
     * */
    public User getCurrentUser();

    /**
     * 分页查询用户列表
     * @param start
     * @param limit
     * @param map
     * @return
     * */
    public IMessage quUserListByPage(Map<String,Object> map,int start,int limit);

    /**
     * 批量删除用户
     * @param
     * */
    public IMessage delUserInfos(List<String> ids);

    /**
     * 修改用户信息
     * @param map
     * @return
     * */
    public IMessage updateUser(Map<String,String> map);
}
