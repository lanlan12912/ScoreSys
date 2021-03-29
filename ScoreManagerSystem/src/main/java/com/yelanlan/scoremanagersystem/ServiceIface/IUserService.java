package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.IUser;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

public interface IUserService {
    public User findUserByNumber(String userNumber);
    public IUser identifyLogin(String userNumber, String userPwd);
    public boolean createUser(User user);
}
