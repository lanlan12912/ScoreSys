package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Repository_Iface.Common.*;
import com.yelanlan.scoremanagersystem.Repository_Impl.Common.*;
import com.yelanlan.scoremanagersystem.Service_Impl.UserService;
import com.yelanlan.scoremanagersystem.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public IMessage Login(@RequestBody Map<String,Object> map){
        try{
            if(ParamUtils.allNotNull(map)){
                return new Message(false,"请输入账号密码");
            }
            if(ParamUtils.allNotNull(map.get("userNumber"),map.get("userPwd"))){
                return new Message(false,"账号密码必能为空");
            }
            //验证用户名，密码是否正确
            if(userService.identifyLogin(map.get("userNumber").toString(),map.get("userPwd").toString())){
                return  new Message(true,"登录成功");
            }else {
                return new Message(false,"密码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,e.getMessage());
        }
    }
}
