package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.MenuDAO;
import com.yelanlan.scoremanagersystem.DAO.RoleResDAO;
import com.yelanlan.scoremanagersystem.Enum.UserRoleEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MenuDTO;
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
        Menu menu = null;
        if(!ParamUtils.allNotNull(menuId)){
            return menu;
        }
        return menuDAO.findMenuByMenuId(menuId);
    }

    /**
     * 保存菜单信息需
     * @param map
     * @rturn
     * */
    @Override
    public IMessage saveMenuInfo(Map<String,Object> map){
        try {
            //生成menuId
            String menuId = UUID.randomUUID().toString().replaceAll("-", "");
            User currentUser = userService.getCurrentUser();
            //创建时间
            Timestamp crtDate = DateUtils.getCurrentTime();
            Menu menu = new Menu(menuId,String.valueOf(map.get("menuName")),String.valueOf(map.get("menuIcon")),String.valueOf(map.get("menuPath")),String.valueOf(map.get("type")),
                    Integer.parseInt(String.valueOf(map.get("order"))), currentUser.getUserNumber(),crtDate,currentUser.getUserNumber(),crtDate);
            if(ParamUtils.allNotNull(map.get("parentId"))){//当父菜单不为空时，该菜单为叶子菜单
                menu.setParentId(String.valueOf(map.get("parentId")));
                menu.setLeafFlag(1);//1为叶子节点，0为不是叶子节点
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
                for(Menu menu : menuList){
                    if(ParamUtils.allNotNull(menu.getParentId()))//父菜单id为空为根节点
                    {
                        MenuDTO dto = new MenuDTO(menu.getMenuId(),menu.getMenuName(),menu.getMenuIcon(),menu.getMenuPath(),menu.getType(),menu.getLeafFlag(),menu.getOrder());
                        dto.setChildPages(menuChild(menu.getMenuId(),menuList));
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
            if(m.getMenuId() == parentId){
                MenuDTO dto = new MenuDTO(m.getMenuId(),m.getMenuName(),m.getMenuIcon(),m.getMenuPath(),m.getType(),m.getLeafFlag(),m.getOrder());
                dto.setChildPages(menuChild(m.getParentId(),menuCommon));
                lists.add(dto);
            }
        }
        return lists;
    }
}
