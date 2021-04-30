package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Enum.DepartEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.ServiceIface.IDepartmentService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;
    @RequestMapping("/saveDepartment")
    public IMessage saveDepartment(@RequestBody Map<String,String> map){
        try {
            if(!ParamUtils.allNotNull(map.get("departName"),map.get("departType"))){
                return new Message(false,"参数不完整");
            }
            if(ParamUtils.allNotNull(map.get("id"))){
                return departmentService.updateDepart(map);
            }
            return departmentService.saveDepart(map);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/delDepart")
    public IMessage delDepart(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"请选择需要删除的院系");
        }
        return departmentService.delDepartment(map.get("id"));
    }

    @RequestMapping("/quDepartTree")
    public IMessage quDepartTree(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("departType"))){
            return new Message(false,"请指定类型");
        }
        DepartEnum type = DepartEnum.valueOf(map.get("departType"));
        return departmentService.quDepartTree(type);
    }

    @RequestMapping("/quDepartInfo")
    public IMessage quDepartInfo(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"请输入院系id");
        }
        return departmentService.quDepartById(map.get("id"));
    }

}
