package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.ServiceIface.IMenuService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @RequestMapping("/saveMemu")
    public IMessage saveMenuInfo(@RequestBody Map<String,Object> map){
        try{
            if(!ParamUtils.allNotNull(map,map.get("menuName"),map.get("menuIcon"),map.get("menuPath"),map.get("type"),
                    map.get("order"))){
                return new Message(false,"参数不完整");
            }
            return menuService.saveMenuInfo(map);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/quMemuList")
    public IMessage quMenuList(){
        return menuService.quUserMenuList();
    }
}
