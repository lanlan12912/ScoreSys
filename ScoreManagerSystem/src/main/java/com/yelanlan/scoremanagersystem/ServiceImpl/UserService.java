package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.DepartmentDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.UserRoleEnum;
import com.yelanlan.scoremanagersystem.Enum.UserStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.UserDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    UserDAO userDAO ;
    private String initPwd = "q1w2E#R$";
    @Autowired
    DepartmentDAO departmentDAO;
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
    public User identifyLogin(String userNumber, String userPwd,boolean isModify){
        User user = null;
        try {
            //先从数据库中查询用户
            user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){//该用户不存在
                return null;
            }
            //用户存在，验证密码是否正确
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String value = baseEncoder.encode(md5.digest(userPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(value)){//密码正确，返回用户信息
                return user;
            }else {//密码不正确，返回null；
                if(isModify){//修改密码无需锁定账户
                    return null;
                }
                // TODO: 2021/3/30  密码不正确，可以做一些锁定账户的操作
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存用户信息，即新建用户时用户信息的初始化
     * @param user
     * @return
     * */
    @Override
    public boolean createUser(User user) {
        try {
            String userNumber = DateUtils.transCurrDateToNum();//使用时间作为生成账号
            //将用户的初始密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String userPwd = baseEncoder.encode(md5.digest(initPwd.getBytes("utf-8")));
            //用户状态为启用
            String userState = UserStateEnum.START.toString();
            //创建时间
            Timestamp userCrtDate = DateUtils.getCurrentTime();
            user.setUserNumber(userNumber);
            user.setUserPwd(userPwd);
            user.setUserState(userState);
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
    public Message modifyPwd(String userNumber, String oldPwd,String newPwd){
        try {
            User user = findUserByNumber(userNumber);
            if(user == null){
                return new Message(false,"用户不存在");
            }
            //验证旧密码是否正确
            if(null == identifyLogin(user.getUserNumber(),oldPwd,true)){
                return new Message(false,"密码错误");
            }
            //将用户的新密码加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String password = baseEncoder.encode(md5.digest(newPwd.getBytes("utf-8")));
            //将加密的放入user实体
            user.setUserPwd(password);
            userDAO.setFixedUserNumber(user.getUserNumber(),user.getUserPwd());//更新密码
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
                        predicateList.add(criteriaBuilder.equal(root.get("userName"),String.valueOf(map.get("userName"))));
                    }
                    if(ParamUtils.allNotNull(map.get("userRole"))){
                        predicateList.add(criteriaBuilder.equal(root.get("userRole"),String.valueOf(map.get("userRole"))));
                    }
                    if(ParamUtils.allNotNull(map.get("collegeId"))){
                        predicateList.add(criteriaBuilder.equal(root.get("collegeId"),String.valueOf(map.get("collegeId"))));
                    }
                    if(ParamUtils.allNotNull(map.get("departmentId"))){
                        predicateList.add(criteriaBuilder.equal(root.get("departmentId"),String.valueOf(map.get("departmentId"))));
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
                List<String> departments = users.stream().map(User::getDepartmentId).collect(Collectors.toList());
                List<Department> departmentList = departmentDAO.findAllByIds(departments);
                for (User user : users) {
                    //根据院id寻找到院信息
                    Department college  = departmentList.stream().filter(coll -> user.getCollegeId().equals(coll.getId())).findAny().orElse(null);
                    //根据系id寻找到系信息
                    Department department = departmentList.stream().filter(depart -> user.getDepartmentId().equals(depart.getId())).findAny().orElse(null);
                    UserDTO userDTO = new UserDTO(user.getUserNumber(), user.getUserName(),UserStateEnum.valueOf(user.getUserState()).getName(), UserRoleEnum.valueOf(user.getUserRole()).getName() ,
                            user.getUserTeleno(),college == null ? "": college.getDepartName(), department == null ?"":department.getDepartName());
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

}
