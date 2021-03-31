package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.IUser;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

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
     * @return
     * */
    public IUser identifyLogin(String userNumber, String userPwd);

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * @param user
     * @return
     * */
    public boolean createUser(User user);

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
}
