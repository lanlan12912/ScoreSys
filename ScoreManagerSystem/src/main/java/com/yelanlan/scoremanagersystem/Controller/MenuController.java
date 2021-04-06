package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Enum.MenuTypeEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
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
            if(!ParamUtils.allNotNull(map,map.get("menuName"),map.get("menuIcon"),map.get("type"),
                    map.get("order"))){
                return new Message(false,"参数不完整");
            }
            if(ParamUtils.allNotNull(map.get("menuId"))){
                return menuService.updateMenu(map);
            }
            return menuService.saveMenuInfo(map);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 查询导航菜单
     * */
    @RequestMapping("/quMemuList")
    public IMessage quMenuList(){
        return menuService.quUserMenuList();
    }

    /**
     * 查询菜单模块
     * */
    @RequestMapping("/getMenuModule")
    public IMessage getMenuModule(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map,map.get("type"))){
            return new Message(false,"菜单类型为空");
        }
        String type = String.valueOf(map.get("type"));
        if(MenuTypeEnum.valueOf(type) == MenuTypeEnum.MODULE){
            return menuService.getMenuModule(MenuTypeEnum.MODULE);
        }else{
            return menuService.getMenuModule(MenuTypeEnum.PAGE);
        }
    }

    @RequestMapping("/quMenuInfo")
    public IMessage quMenuInfo(@RequestBody Map<String,Object> map){
        try {
            if(!ParamUtils.allNotNull(map,map.get("menuId"))){
                return new Message(false,"参数id为空，无法查询到指定菜单");
            }
            String menuId = String.valueOf(map.get("menuId"));
            Menu menu = menuService.quMenuById(menuId);
            if(menu == null){
                return new Message(false,"没有查询到该菜单");
            }else {
                Message message = new Message(true,"查询成功");
                message.setData(menu);
                return message;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
