package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.MenuDAO;
import com.yelanlan.scoremanagersystem.DAO.RoleResDAO;
import com.yelanlan.scoremanagersystem.Enum.MenuTypeEnum;
import com.yelanlan.scoremanagersystem.Enum.UserRoleEnum;
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
            if(ParamUtils.allNotNull(map.get("parentId"))){//当父菜单不为空时，该菜单为叶子菜单
                menu.setParentId(String.valueOf(map.get("parentId")));
                menu.setParentName(String.valueOf(map.get("parentName")));
                menu.setLeafFlag(1);//1为叶子节点，0为不是叶子节点
                menu.setMenuPath(String.valueOf(map.get("menuPath")));
            }else{
                menu.setLeafFlag(0);//1为叶子节点，0为不是叶子节点
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
            switch (UserRoleEnum.valueOf(user.getUserRole())){
                case ADMIN://系统管理员菜单
                    menuList = menuDAO.findAll();//所有菜单
                    break;
                default://其他
                    //根据用户角色查询查询角色被授予的资源有哪些
                    List<RoleRes> roleResList = roleResDAO.findAllByRoleId(user.getUserRole());
                    //根据资源id查询出菜单列表
                    for(RoleRes res :roleResList){
                        Menu menuobj = menuDAO.findMenuByMenuId(res.getResId()) ;
                        if(!ParamUtils.allNotNull(menuobj)){
                            menuList.add(menuobj);
                        }
                    }
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
                         treeDTO.setChildren(dealMenuToTree(menu.getChildPages()));
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
    public List<MenuTreeDTO> dealMenuToTree(List<MenuDTO> menuList){
        List<MenuTreeDTO> lists = new ArrayList<>();
        for(MenuDTO menu: menuList){
            MenuTreeDTO dto = new MenuTreeDTO(menu.getMenuId(),menu.getMenuName(),false,true);
            if(menu.getChildPages().size()>0){
                dealMenuToTree(menu.getChildPages());
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
            menuDAO.save(menu);//将更新的菜单信息保存
            return new Message(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
