package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.ServiceIface.IRoleService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    private IRoleService roleService;
    /**
     * 分页查询所有角色信息
     * */
    @RequestMapping("/getAllRoles")
    public IMessage getAllRoles(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
            return new Message(false,"分页参数异常");
        }
        int start = Integer.parseInt(map.get("start"));
        int limit = Integer.parseInt(map.get("limit"));
        return  roleService.quAllRoles(start,limit);
    }
    /**
     * 添加角色信息
     * */
    @RequestMapping("/addRole")
    public IMessage addRole(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("roleName"))){
            return new Message(false,"角色名为空");
        }
        String roleDes = null;
        if(ParamUtils.allNotNull(map.get("roleDes"))){
            roleDes = map.get("roleDes");
        }
        return null;

    }


}
