package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceImpl.UserService;
import com.yelanlan.scoremanagersystem.Utils.JwtUtil;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public IMessage Login(@RequestBody Map<String,Object> map, HttpServletResponse response){
        try{
            if(!ParamUtils.allNotNull(map)){
                return new Message(false,"请输入账号密码");
            }
            if(!ParamUtils.allNotNull(map.get("userNumber"),map.get("userPwd"))){
                return new Message(false,"账号密码必能为空");
            }
            //验证用户名，密码是否正确
            User user = userService.identifyLogin(map.get("userNumber").toString(),map.get("userPwd").toString());
            if(null != user){
                //生成Token令牌
                Cookie cookie = new Cookie("token", JwtUtil.getToken(user));
                cookie.setPath("/");
                response.addCookie(cookie);
                return  new Message(true,"登录成功");
            }else {
                return new Message(false,"用户名或密码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
