package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.UserStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Objects;

@Service
public class UserService implements IUserService {
    @Autowired
    UserDAO userDAO ;
    private String initPwd = "q1w2E#R$";
    /**
     * 当前账号常量
     */
    private static final String CURRENTUSER = "account";

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取当前登录的用户
     * @return
     * */
    public User getCurrentUser() {
        HttpSession session = getRequest().getSession();
        return (User) session.getAttribute(CURRENTUSER);
    }

    /**
     * 保存当前登陆的用户
     * @param  user
     * @return
     * */
    public void setCurrentuser(User user) {
        HttpSession session = getRequest().getSession();
        if (user != null) {
            session.setAttribute(CURRENTUSER, user);
            //session过期时间设置，以秒为单位，即在没有活动30分钟后，session将失效
            session.setMaxInactiveInterval(30 * 60);
        }
    }

    /**
     * 根据用户账号查找用户信息
     * @param userNumber
     * @return
     * */
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

    /**
     * 验证登录密码是否正确
     * @param  userNumber
     * @param  userPwd
     * @return
     * */
    @Override
    public User identifyLogin(String userNumber, String userPwd){
        User user = null;
        try {
            //先从数据库中查询用户
            user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){//该用户不存在
                return null;
            }
            //用户存在，验证密码是否正确
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String value = baseEncoder.encode(md5.digest(userPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(value)){//密码正确，返回用户信息
                return user;
            }else {//密码不正确，返回null；
                // TODO: 2021/3/30  密码不正确，可以做一些锁定账户的操作
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * @param user
     * @return
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

    /**
     *修改密码
     * @param userNumber
     * @param oldPwd
     * @param newPwd
     * @return
     * */
    @Override
    public Message modifyPwd(String userNumber, String oldPwd,String newPwd){
        try {
            User user = findUserByNumber(userNumber);
            if(user == null){
                return new Message(false,"用户不存在");
            }
            //将用户的初始密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String password = baseEncoder.encode(md5.digest(initPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(password)){
                return new Message(false,"新密码不能号旧密码一样");
            }
            user.setUserPwd(password);
            userDAO.setFixedUserNumber(user.getUserNumber(),user.getUserPwd());
            return new Message(true,"密码修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

}
