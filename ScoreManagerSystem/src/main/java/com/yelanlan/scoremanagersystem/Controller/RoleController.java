package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;
import com.yelanlan.scoremanagersystem.ServiceIface.IRoleService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {
    @Autowired
    private IRoleService roleService;

    /**
     * 分页查询所有角色信息
     */
    @RequestMapping("/getAllRoles")
    public IMessage getAllRoles(@RequestBody Map<String, String> map) {
        try {
            if (!ParamUtils.allNotNull(map.get("start"), map.get("limit"))) {
                return new Message(false, "分页参数异常");
            }
            int start = Integer.parseInt(map.get("start"));
            int limit = Integer.parseInt(map.get("limit"));
            return roleService.quAllRoles(start, limit);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 添加角色信息
     */
    @RequestMapping("/addRole")
    public IMessage addRole(@RequestBody Map<String, String> map) {
        if (!ParamUtils.allNotNull(map.get("roleName"))) {
            return new Message(false, "角色名为空");
        }
        Role role = new Role();
        role.setRoleName(map.get("roleName"));
        if (null != map.get("roleDes") && !"".equals(map.get("roleDes"))) {
            role.setRoleDes(map.get("roleDes"));
        }
        if(null != map.get("roleId") && !"".equals(map.get("roleId"))){
            role.setRoleId(map.get("roleId"));
        }
        return roleService.addRole(role);
    }

    /**
     * 删除角色信息
     */
    @RequestMapping("/delRoles")
    public IMessage delRoles(@RequestBody Map<String, Object> map) {
        if(!ParamUtils.allNotNull(map.get("roles"))){
            return new Message(false,"没有选中角色，无法删除");
        }
        return roleService.delRoles((List<String>) map.get("roles"));
    }

    /**
     * 获取资源树
     *
     * */
    @RequestMapping("/getResTree")
    public IMessage getResTree(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("roleId"))){
            return new Message(false,"角色Id为空，无法查询相关资源");
        }
        return roleService.getAuthMenuRes(map.get("roleId"));
    }

    /**
     * 授权
     * */
    @RequestMapping("/authMenuToRole")
    public IMessage authMenuToRole(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map.get("roleId"))){
            return new Message(true,"没有指定角色");
        }
        String roleId = String.valueOf(map.get("roleId"));
        List<String> resIds = (List<String>) map.get("resIds");
        return  roleService.authorization(roleId,resIds);
    }
}
