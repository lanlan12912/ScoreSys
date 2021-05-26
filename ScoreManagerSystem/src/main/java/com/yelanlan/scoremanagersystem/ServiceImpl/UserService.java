package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.DepartmentDAO;
import com.yelanlan.scoremanagersystem.DAO.RoleResDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.UserRankEnum;
import com.yelanlan.scoremanagersystem.Enum.UserStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.UserDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.FileUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    UserDAO userDAO ;
    private String initPwd = "q1w2E#R$";
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    RoleResDAO roleResDAO;
    @Autowired
    private Environment environment;

    /**
     * 当前账号常量
     */
    private static final String CURRENTUSER = "account";

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取当前登录的用户
     * @return
     * */
    public User getCurrentUser() {
        HttpSession session = getRequest().getSession();
        return (User) session.getAttribute(CURRENTUSER);
    }

    /**
     * 保存当前登陆的用户
     * @param  user
     * @return
     * */
    public void setCurrentuser(User user) {
        HttpSession session = getRequest().getSession();
        if (user != null) {
            session.setAttribute(CURRENTUSER, user);
            //session过期时间设置，以秒为单位，即在没有活动30分钟后，session将失效
            session.setMaxInactiveInterval(30 * 60);
        }
    }

    /**
     * 根据用户账号查找用户信息
     * @param userNumber
     * @return
     * */
    @Override
    public User findUserByNumber(String userNumber){
        User user = null;
        if(userNumber.equals("") ||null == userNumber){
            user = null;
        }else {
            user=  userDAO.findUserByUserNumber(userNumber);
        }
        return user;
    }

    /**
     * 验证登录密码是否正确
     * @param  userNumber
     * @param  userPwd
     * @return
     * */
    @Override
    public IMessage identifyLogin(String userNumber, String userPwd,boolean isModify){
        try {
            //先从数据库中查询用户
            User user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){//该用户不存在
                return new Message(false,"用户不存在");
            }
            if(UserStateEnum.valueOf(user.getUserState()) == UserStateEnum.STOP){//用户和已被停用
                return new Message(false,"用户已被停用，请联系管理员");
            }
            //用户存在，验证密码是否正确
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String value = baseEncoder.encode(md5.digest(userPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(value)){//密码正确，返回用户信息
                Message message = new Message(true,"登录成功");
                message.setData(user);
                return message;
            }else {//密码不正确，返回null；
                if(isModify){//修改密码无需锁定账户
                    return null;
                }
                // TODO: 2021/3/30  密码不正确，可以做一些锁定账户的操作
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * @param map
     * @return
     * */
    @Override
    public boolean createUser(Map<String,String> map) {
        try {
            User user = new User();
            user.setUserName(map.get("userName"));
            user.setDepartmentId(map.get("departmentId"));
            user.setUserRank(UserRankEnum.valueOf(map.get("userRank")).toString());
            if(!ParamUtils.allNotNull(map.get("userState"))){
                user.setUserState(UserStateEnum.START.toString());
            }
            user.setUserState(map.get("userState"));
            user.setUserTeleno(ParamUtils.allNotNull(map.get("userTeleno"))?map.get("userTeleno"):"");
            user.setUserDesc(ParamUtils.allNotNull(map.get("userDesc"))?map.get("userDesc"):"");
            String userNumber = DateUtils.transCurrDateToNum();//使用时间作为生成账号
            //将用户的初始密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String userPwd = baseEncoder.encode(md5.digest(initPwd.getBytes("utf-8")));
            //创建时间
            Timestamp userCrtDate = DateUtils.getCurrentTime();
            user.setUserNumber(userNumber);
            user.setUserPwd(userPwd);
            user.setUserCrtdate(userCrtDate);
            user.setFailedLoginCount(0);
            userDAO.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *修改密码
     * @param userNumber
     * @param oldPwd
     * @param newPwd
     * @return
     * */
    @Override
    public IMessage modifyPwd(String userNumber, String oldPwd,String newPwd){
        try {
            User user = findUserByNumber(userNumber);
            if(user == null){
                return new Message(false,"用户不存在");
            }
            IMessage message = identifyLogin(user.getUserNumber(),oldPwd,true);
            //验证旧密码是否正确
            if(!message.isSuccess() ){
                return message;
            }
            //将用户的新密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String password = baseEncoder.encode(md5.digest(newPwd.getBytes("utf-8")));
            //将加密的放入user实体
            user.setUserPwd(password);
            userDAO.updateUserNumber(user.getUserPwd(),user.getUserNumber());//更新密码
            return new Message(true,"密码修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 分页查询用户列表
     * @param start
     * @param limit
     * @param map
     * @return
     * */
    @Override
    public IMessage quUserListByPage(Map<String,Object> map,int start,int limit){
        try {
            Specification<User> spec = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    if(ParamUtils.allNotNull(map.get("userNumber"))){
                        predicateList.add(criteriaBuilder.equal(root.get("userNumber"),String.valueOf(map.get("userNumber"))));
                    }
                    if(ParamUtils.allNotNull(map.get("userName"))){
                        predicateList.add(criteriaBuilder.like(root.get("userName"),String.valueOf(map.get("userName"))));
                    }
                    if(ParamUtils.allNotNull(map.get("departmentIds"))){
                        CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("departmentId"));
                        List<String> departmentIds = Arrays.asList(String.valueOf(map.get("departmentIds")).split(","));
                        if(departmentIds.size()>0) {
                            for(String s : departmentIds){
                                in.value(s);
                            }
                            predicateList.add(in);
                        }
                    }
                    if(ParamUtils.allNotNull(map.get("userState"))){
                        predicateList.add(criteriaBuilder.equal(root.get("userState"),String.valueOf(map.get("userState"))));
                    }
                    return criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
                }
            };
            Page<User> userPage = userDAO.findAll(spec,PageRequest.of(start-1,limit));
            List<User> users = userPage.getContent();
            List<UserDTO> userDTOS = new ArrayList<>();
            if(users.size()>0){
                for (User user : users) {
                    //根据系id寻找院系信息
                    Department department = departmentDAO.findDepartById(user.getDepartmentId());
                    UserDTO userDTO = new UserDTO(user.getUserNumber(), user.getUserName(),user.getUserState(),UserStateEnum.valueOf(user.getUserState()).getName(), user.getUserRank(),UserRankEnum.valueOf(user.getUserRank()).getName(),
                            user.getUserTeleno(),user.getDepartmentId(), department == null ?"":department.getDepartName(),user.getUserDesc());
                    userDTOS.add(userDTO);
                }
            }
            Page<UserDTO> userDTOPage = new PageImpl(userDTOS,PageRequest.of(start-1,limit),userPage.getTotalElements());
            Message message = new Message(true,"查询成功");
            message.setData(userDTOPage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 批量删除用户
     * @param
     * */
    @Override
    public IMessage delUserInfos(List<String> ids){
        try {
            if(ids.size()>0){
                userDAO.deleteAllByUserNumbers(ids);
                //删除给用户分配的角色关联关系
                roleResDAO.deleteAllByRoleIds(ids);
                return new Message(true,"删除成功");
            }
            return new Message(false,"没有可删除的信息");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 修改用户信息
     * @param map
     * @return
     * */
    @Override
    public IMessage updateUser(Map<String,String> map){
        try {
            String userNumber = map.get("userNumber");
            User user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){
                return new Message(false,"该用户已不存在，无法修改");
            }
            //先将用户删除
            userDAO.delete(user);
            user.setUserName(map.get("userName"));
            user.setUserState(map.get("userState"));
            user.setDepartmentId(map.get("departmentId"));
            user.setUserRank(map.get("userRank"));
            user.setUserTeleno(map.get("userTeleno"));
            user.setUserDesc(map.get("userDesc"));
            //插入新的数据
            userDAO.save(user);
            return new Message(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 上传头像
     * @param imgFile
     * @param imgType
     * @return
     * */
    @Override
    public IMessage uploadHeadAvatar(String imgFile, String imgType){
        try {
            String path = environment.getProperty("spring.headImgPath")+"/";//#头像图片路径
            String staticPath = environment.getProperty("spring.staticHeadImg")+"/";
            String name = UUID.randomUUID().toString().replaceAll("-","");
            String staticFullPath = "";
            Message message = new Message();
            staticFullPath = staticPath+name+imgType;
            User curUser = getCurrentUser();
            if(curUser == null){
                return new Message(false,"请登录后操作");
            }
            User user = userDAO.findUserByUserNumber(getCurrentUser().getUserNumber());
            if(null == user){
                return new Message(false,"用户已不存在");
            }
            boolean flag= FileUtils.writeImg(imgFile,imgType,name,path);
            if (flag) {
                if (ParamUtils.allNotNull(user.getHeadAvatar())) {//更换头像前需将之前的头像删除
                    if (FileUtils.deleteImg(path, user.getHeadAvatar().replaceAll(staticPath, ""))) {
                        userDAO.updateUserHead(staticFullPath, getCurrentUser().getUserNumber());//删除成功，更新数据库
                    } else {
                        return new Message(false, "删除原头像失败");
                    }
                } else {
                    userDAO.updateUserHead(staticFullPath, getCurrentUser().getUserNumber());
                }
                message.setMsg("上传成功");
                message.setSuccess(true);
                message.setData(staticFullPath);
                return message;
            } else {
                message.setMsg("头像上传失败");
                message.setSuccess(false);
                return message;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

}
