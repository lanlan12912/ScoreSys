package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.MenuDAO;
import com.yelanlan.scoremanagersystem.DAO.RoleResDAO;
import com.yelanlan.scoremanagersystem.Enum.MenuTypeEnum;
import com.yelanlan.scoremanagersystem.Enum.UserRankEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MenuDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MenuTreeDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
import com.yelanlan.scoremanagersystem.RepositoryImpl.RoleRes;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IMenuService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenuService implements IMenuService {
    @Autowired
    private MenuDAO menuDAO;
    @Autowired
    private RoleResDAO roleResDAO;
    @Autowired
    private UserService userService;

    /**
     * 根据menuId查询菜单信息
     * @param menuId
     * @return
     * */
    @Override
    public Menu quMenuById(String menuId){
        return menuDAO.findMenuByMenuId(menuId);
    }

    /**
     * 保存菜单信息
     * @param map
     * @rturn
     * */
    @Override
    public IMessage saveMenuInfo(Map<String,Object> map){
        try {
            //判断 是否已经存在菜单，若不存在生成menuId
            String menuId = UUID.randomUUID().toString().replaceAll("-", "");
            User currentUser = userService.getCurrentUser();
            //创建时间
            Timestamp crtDate = DateUtils.getCurrentTime();
            Menu menu = new Menu(menuId,String.valueOf(map.get("menuName")),String.valueOf(map.get("menuIcon")),String.valueOf(map.get("type")),
                    Integer.parseInt(String.valueOf(map.get("orders"))), currentUser.getUserNumber(),crtDate,currentUser.getUserNumber(),crtDate);
            if(null == map.get("parentId") || "".equals(String.valueOf(map.get("parentId")))){
                menu.setLeafFlag(0);//1为叶子节点，0为不是叶子节点
            }else {//当父菜单不为空时，该菜单为叶子菜单
                menu.setParentId(String.valueOf(map.get("parentId")));
                menu.setParentName(String.valueOf(map.get("parentName")));
                menu.setLeafFlag(1);//1为叶子节点，0为不是叶子节点
            }
            if(MenuTypeEnum.valueOf(String.valueOf(map.get("type")))== MenuTypeEnum.PAGE){
                menu.setMenuPath(String.valueOf(map.get("menuPath")));
            }
          menuDAO.save(menu);//保存菜单信息
            return new Message(true,"保存成功");
        }catch (Exception e ){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 查询当前用户已授权的菜单
     * @return
     * */
    @Override
    public IMessage quUserMenuList(){
        try{
            User user = userService.getCurrentUser();
            if(user == null){
                return new Message(false,"请登录后操作");
            }
            List<MenuDTO>  menuDTOS = new ArrayList<>();
            List<Menu> menuList = new ArrayList<>();
            //获取菜单列表
            switch (UserRankEnum.valueOf(user.getUserRank())){
                case ADMIN://系统管理员菜单
                    menuList = menuDAO.findAllOrOrderByOrders();//所有菜单
                    break;
                default://其他
                    //根据用户角色查询查询角色被授予的资源有哪些
                    //资源配置表role为用户编号，资源为角色id
                    List<RoleRes> roleResList = roleResDAO.findAllByRoleId(user.getUserNumber());
                    //根据角色查询出与菜单资源的配置关系
                    roleResList = roleResDAO.findAllByRoleIds(roleResList.stream().map(RoleRes::getResId).collect(Collectors.toList()));
                    List<String> menuIds = roleResList.stream().map(RoleRes::getResId).collect(Collectors.toList());
                    //根据资源id查询出菜单列表
                    menuList = menuDAO.findAllByMenuIdIn(menuIds);
                    break;
            }
            //将菜单分层处理
            if(menuList.size()>0){
                //去除根节点
                List<Menu> childmenu = menuList.stream().filter(menu -> null != menu.getParentId() && !"".equals(menu.getParentId())).collect(Collectors.toList());
                for(Menu menu : menuList){
                    if(!ParamUtils.allNotNull(menu.getParentId()))//父菜单id为空为根节点
                    {
                        MenuDTO dto = new MenuDTO(menu.getMenuId(),menu.getMenuName(),menu.getMenuIcon(),menu.getMenuPath(),menu.getType(),menu.getLeafFlag(),menu.getOrders());
                        dto.setChildPages(menuChild(menu.getMenuId(),childmenu));
                        menuDTOS.add(dto);
                    }
                }
            }
            menuDTOS = menuDTOS.stream().filter(item->item.getChildPages().size()>0).collect(Collectors.toList());
            Message message = new Message(true,"查询成功");
            message.setData(menuDTOS);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 获取子菜单
     * */
    public List<MenuDTO> menuChild(String parentId,List<Menu> menuCommon){
        List<MenuDTO> lists = new ArrayList<>();
        for(Menu m : menuCommon){
            if(m.getParentId().equals(parentId)){
                MenuDTO dto = new MenuDTO(m.getMenuId(),m.getMenuName(),m.getMenuIcon(),m.getMenuPath(),m.getType(),m.getLeafFlag(),m.getOrders());
                dto.setChildPages(menuChild(m.getParentId(),menuCommon.stream().filter(menu -> !menu.getMenuId().equals(m.getMenuId())).collect(Collectors.toList())));
                lists.add(dto);
            }
        }
        return lists;
    }

    /**
     * 获取菜单模块
     * @return
     * */
    @Override
    public IMessage getMenuModule( MenuTypeEnum typeEnum){
        try {
            IMessage msg = quUserMenuList();
            if(!msg.isSuccess()){
                return msg;
            }else {//成功获取到菜单后
             List<MenuDTO> menuList = (List<MenuDTO>) msg.getData();
             if(typeEnum == MenuTypeEnum.MODULE){
                 menuList = menuList.stream().filter(item -> MenuTypeEnum.valueOf(item.getType())== MenuTypeEnum.MODULE).collect(Collectors.toList());//过滤出模块
             }
             List<MenuTreeDTO> menuTree = new ArrayList<>();
             if(menuList.size()>0){
                 for(MenuDTO menu:menuList){//遍历
                     MenuTreeDTO treeDTO = new MenuTreeDTO(menu.getMenuId(),menu.getMenuName(),false,true);
                     if(menu.getChildPages().size()>0){
                         treeDTO.setChildren(dealMenuToTree(menu.getChildPages(),typeEnum));
                     }
                     menuTree.add(treeDTO);
                 }
             }
             msg.setData(menuTree);
             return msg;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 处理子菜单为树
     *
     * */
    public List<MenuTreeDTO> dealMenuToTree(List<MenuDTO> menuList,MenuTypeEnum typeEnum){
        List<MenuTreeDTO> lists = new ArrayList<>();
        if(MenuTypeEnum.MODULE == typeEnum){
            menuList = menuList.stream().filter(item -> MenuTypeEnum.valueOf(item.getType())== MenuTypeEnum.MODULE).collect(Collectors.toList());//过滤出模块
        }
        for(MenuDTO menu: menuList){
            MenuTreeDTO dto = new MenuTreeDTO(menu.getMenuId(),menu.getMenuName(),false,true);
            if(menu.getChildPages().size()>0){
                dealMenuToTree(menu.getChildPages(),typeEnum);
            }
            lists.add(dto);
        }
        return lists;
    }

    /**
     * 更新菜单信息
     * @param map
     * @return
     * */
    @Override
    public IMessage updateMenu(Map<String,Object> map){
        try{
            if(!ParamUtils.allNotNull(map.get("menuId"))){
                return new Message(false,"找不到菜单");
            }
            Menu menu = menuDAO.findMenuByMenuId(String.valueOf(map.get("menuId")));
            //先将已存在的菜单删除
            menuDAO.delete(menu);
            menu.setMenuName(String.valueOf(map.get("menuName")));
            menu.setMenuIcon(String.valueOf(map.get("menuIcon")));
            menu.setOrders(Integer.parseInt(String.valueOf(map.get("orders"))));
            menu.setModifyDate(DateUtils.getCurrentTime());
            menu.setModifyUser(userService.getCurrentUser().getUserNumber());
            //上级菜单与路径不能更改，无需处理
            menuDAO.save(menu);//将更新的菜单信息保存
            return new Message(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 根据id删除菜单
     * @param menuId
     * @return
     * */
    @Override
    public IMessage delMenu(String menuId){
        try{
            //先查看菜单是否存在
            Menu menu = menuDAO.findMenuByMenuId(menuId);
            if(!ParamUtils.allNotNull(menu)){
                return new Message(false,"菜单已不存在");
            }
            List<Menu> childMenus = menuDAO.findAllByParentId(menuId);//子菜单/子模块
            List<String> menuIds = childMenus.stream().map(Menu::getMenuId).collect(Collectors.toList());
            menuIds.add(menuId);
            if (roleResDAO.findAllByResIds(menuIds).size()>0){
                return new Message(false,"菜单/子菜单 已被授权给用户使用，无法删除");
            }
            //如果菜单为页面则删除页面
            if(MenuTypeEnum.valueOf(menu.getType()) == MenuTypeEnum.PAGE){
                menuDAO.delete(menu);
                return new Message(true,"删除成功");
            }else {//如果菜单为模块，则删除其下的模块或页面
                //先删除子菜单
                if(childMenus.size()>0){
                    Message message = delChildMenu(childMenus);
                    if(!message.isSuccess()){
                        return new Message(false,message.getMsg());
                    }
                }
                //最后删除父菜单
                menuDAO.delete(menu);
                return new Message(true,"删除成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 删除子菜单
     * @param  menuList
     * @return
     * */
    public Message delChildMenu(List<Menu> menuList){
        for(Menu menu : menuList){
            List<Menu> childMenus = menuDAO.findAllByParentId(menu.getMenuId());//子菜单/子模块
            List<String> menuIds = childMenus.stream().map(Menu::getMenuId).collect(Collectors.toList());
            menuIds.add(menu.getMenuId());
            if (roleResDAO.findAllByResIds(menuIds).size()>0){
                return new Message(false,"菜单/子菜单 已被授权给用户使用，无法删除");
            }
            if(MenuTypeEnum.valueOf(menu.getType()) == MenuTypeEnum.PAGE){
                menuDAO.delete(menu);
            }else {
                if(childMenus.size()>0){
                    Message message = delChildMenu(childMenus);
                    if(!message.isSuccess()){//删除子菜单失败时立即返回
                        return message;
                    }
                }
                menuDAO.delete(menu);
            }
        }
        return new Message(true,"删除成功");
    }
}
