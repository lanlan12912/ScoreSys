package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Enum.UserRankEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.EnumDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserInfoController {
    @Autowired
    IUserService userService;

    @RequestMapping("/saveUser")
    public IMessage saveUser(@RequestBody Map<String,String> map){
        try {
            if(!ParamUtils.allNotNull(map.get("userName"),map.get("userRank"))){
                return new Message(false,"用户名/身份不明确");
            }
            if(UserRankEnum.STUDENT == UserRankEnum.valueOf(map.get("userRank"))){
                if(!ParamUtils.allNotNull(map.get("departmentId"))){
                    return new Message(false,"学生需要指明院系");
                }
            }
            if(!ParamUtils.allNotNull(map.get("userNumber"))){
                if(userService.createUser(map)){
                    return new Message(true,"保存成功");
                }else {
                    return new Message(false,"保存失败");
                }
            }else{
                return userService.updateUser(map);
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

    /**
     * 分页查询所有用户
     *
     * */
    @RequestMapping("/quUserListByPage")
    public IMessage quUserListByPage(@RequestBody Map<String,Object> map ){
        try{
            if(!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
                return new Message(false,"分页参数异常");
            }
            int start = Integer.parseInt(String.valueOf(map.get("start")));
            int limit = Integer.parseInt(String.valueOf(map.get("limit")));
            return userService.quUserListByPage(map,start,limit);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/delUserInfos")
    public IMessage delUserInfos(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map.get("users"))){
            return new Message(false,"参数异常");
        }
        List<String> ids = (List<String>) map.get("users");
        return userService.delUserInfos(ids);
    }

    /**
     * 获取用户身份
     * @return
     * */
    @RequestMapping("/getUserRanks")
    public IMessage getUserRanks(){
        try {
            List<EnumDTO> rankList = new ArrayList<>();
            if(UserRankEnum.values().length>0) {
                for (UserRankEnum rankEnum : UserRankEnum.values()) {
                    rankList.add(new EnumDTO(rankEnum.toString(),rankEnum.getName()));
                }
            }
            Message message = new Message(true,"查询成功");
            message.setData(rankList);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
