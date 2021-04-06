package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.Enum.MenuTypeEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;

import java.util.Map;

public interface IMenuService {
    /**
     * 根据menuId查询菜单信息
     * @param menuId
     * @return
     * */
    public Menu quMenuById(String menuId);

    /**
     * 保存菜单信息需
     *
     * @param map
     * @rturn
     */
    public IMessage saveMenuInfo(Map<String, Object> map);

    /**
     * 查询当前用户已授权的菜单
     * @return
     * */
    public IMessage quUserMenuList();

    /**
     * 获取菜单模块
     * @param typeEnum
     * @return
     * */
    public IMessage getMenuModule(MenuTypeEnum typeEnum);

    /**
     * 更新菜单信息
     * @param map
     * @return
     * */
    public IMessage updateMenu(Map<String,Object> map);

}
