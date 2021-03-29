package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.UserStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.sql.Timestamp;

@Service
public class UserService implements IUserService {
    @Autowired
    UserDAO userDAO ;
    private String initPwd = "q1w2E#R$";

    @Override
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
    public User identifyLogin(String userNumber, String userPwd){
        User user = null;
        try {
            //先从数据库中查询用户
            user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){//该用户不存在
                return null;
            }
            //用户存在，验证米密码是否正确
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String value = baseEncoder.encode(md5.digest(userPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(value)){//密码正确，返回用户信息
                return user;
            }else {//密码不正确，返回null；
                //密码不正确，可以做一些锁定账户的操作
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * */
    @Override
    public boolean createUser(User user) {
        try {
            String userNumber = DateUtils.transCurrDateToNum();//使用时间作为生成账号
            //将用户的初始密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String userPwd = baseEncoder.encode(md5.digest(initPwd.getBytes("utf-8")));
            //用户状态为启用
            String userState = UserStateEnum.START.toString();
            //创建时间
            Timestamp userCrtDate = DateUtils.getCurrentTime();
            user.setUserNumber(userNumber);
            user.setUserPwd(userPwd);
            user.setUserState(userState);
            user.setUserCrtdate(userCrtDate);
            user.setFailedLoginCount(0);
            userDAO.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
