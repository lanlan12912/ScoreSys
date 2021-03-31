package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserInfoController {
    @Autowired
    IUserService userService;

    @RequestMapping("/saveUser")
    public IMessage saveUser(@RequestBody User user){
        try {
            if(!ParamUtils.allNotNull(user)){
                return new Message(false,"被保存对象不存在");
            }
//            User user = (User) map.get("user");
            if(!ParamUtils.allNotNull(user.getUserName(),user.getUserRole())){
                return new Message(false,"用户名与身份不明确");
            }
            if(userService.createUser(user)){
                return new Message(true,"保存成功");
            }else {
                return new Message(false,"保存失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/getCurrentUser")
    public User getCurrentUser(){
        return userService.getCurrentUser();
    }

    /**
     * 修改密码
     */
    @RequestMapping("/modifyPwd")
    public IMessage modifyPwd(String userNumber,String oldPwd,String newPwd){
        try{
            if(ParamUtils.allNotNull(userNumber,oldPwd,newPwd)){
                return new Message(false,"请将信息填写完整");
            }
            if(newPwd.equals(oldPwd)){
                return new Message(false,"新密码不能与旧密码相同");
            }
            return userService.modifyPwd(userNumber,oldPwd,newPwd);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
