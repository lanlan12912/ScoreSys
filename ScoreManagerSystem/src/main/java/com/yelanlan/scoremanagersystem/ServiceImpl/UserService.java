package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    UserDAO userDAO ;

    public User findUserByNumber(String userNumber){
        User user = null;
        if(userNumber.equals("") ||null == userNumber){
            user = null;
        }else {
            user=  userDAO.findUserByUserNumber(userNumber);
        }
        return user;
    }

    @Override
    public  boolean identifyLogin(String userNumber,String userPwd){
        return false;
    }
}
