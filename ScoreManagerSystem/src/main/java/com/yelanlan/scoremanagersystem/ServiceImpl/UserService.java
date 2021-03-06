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
     * ??????????????????
     */
    private static final String CURRENTUSER = "account";

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * ???????????????????????????
     * @return
     * */
    public User getCurrentUser() {
        HttpSession session = getRequest().getSession();
        return (User) session.getAttribute(CURRENTUSER);
    }

    /**
     * ???????????????????????????
     * @param  user
     * @return
     * */
    public void setCurrentuser(User user) {
        HttpSession session = getRequest().getSession();
        if (user != null) {
            session.setAttribute(CURRENTUSER, user);
            //session?????????????????????????????????????????????????????????30????????????session?????????
            session.setMaxInactiveInterval(30 * 60);
        }
    }

    /**
     * ????????????????????????????????????
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
     * ??????????????????????????????
     * @param  userNumber
     * @param  userPwd
     * @return
     * */
    @Override
    public IMessage identifyLogin(String userNumber, String userPwd,boolean isModify){
        try {
            //??????????????????????????????
            User user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){//??????????????????
                return new Message(false,"???????????????");
            }
            if(UserStateEnum.valueOf(user.getUserState()) == UserStateEnum.STOP){//?????????????????????
                return new Message(false,"???????????????????????????????????????");
            }
            //???????????????????????????????????????
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String value = baseEncoder.encode(md5.digest(userPwd.getBytes("utf-8")));
            if(user.getUserPwd().equals(value)){//?????????????????????????????????
                Message message = new Message(true,"????????????");
                message.setData(user);
                return message;
            }else {//????????????????????????null???
                if(isModify){//??????????????????????????????
                    return null;
                }
                // TODO: 2021/3/30  ??????????????????????????????????????????????????????
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
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
            String userNumber = DateUtils.transCurrDateToNum();//??????????????????????????????
            //??????????????????????????????
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String userPwd = baseEncoder.encode(md5.digest(initPwd.getBytes("utf-8")));
            //????????????
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
     *????????????
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
                return new Message(false,"???????????????");
            }
            IMessage message = identifyLogin(user.getUserNumber(),oldPwd,true);
            //???????????????????????????
            if(!message.isSuccess() ){
                return message;
            }
            //???????????????????????????
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder baseEncoder = new BASE64Encoder();
            String password = baseEncoder.encode(md5.digest(newPwd.getBytes("utf-8")));
            //??????????????????user??????
            user.setUserPwd(password);
            userDAO.updateUserNumber(user.getUserPwd(),user.getUserNumber());//????????????
            return new Message(true,"??????????????????");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

    /**
     * ????????????????????????
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
                    //?????????id??????????????????
                    Department department = departmentDAO.findDepartById(user.getDepartmentId());
                    UserDTO userDTO = new UserDTO(user.getUserNumber(), user.getUserName(),user.getUserState(),UserStateEnum.valueOf(user.getUserState()).getName(), user.getUserRank(),UserRankEnum.valueOf(user.getUserRank()).getName(),
                            user.getUserTeleno(),user.getDepartmentId(), department == null ?"":department.getDepartName(),user.getUserDesc());
                    userDTOS.add(userDTO);
                }
            }
            Page<UserDTO> userDTOPage = new PageImpl(userDTOS,PageRequest.of(start-1,limit),userPage.getTotalElements());
            Message message = new Message(true,"????????????");
            message.setData(userDTOPage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

    /**
     * ??????????????????
     * @param
     * */
    @Override
    public IMessage delUserInfos(List<String> ids){
        try {
            if(ids.size()>0){
                userDAO.deleteAllByUserNumbers(ids);
                //??????????????????????????????????????????
                roleResDAO.deleteAllByRoleIds(ids);
                return new Message(true,"????????????");
            }
            return new Message(false,"????????????????????????");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

    /**
     * ??????????????????
     * @param map
     * @return
     * */
    @Override
    public IMessage updateUser(Map<String,String> map){
        try {
            String userNumber = map.get("userNumber");
            User user = userDAO.findUserByUserNumber(userNumber);
            if(null == user){
                return new Message(false,"????????????????????????????????????");
            }
            //??????????????????
            userDAO.delete(user);
            user.setUserName(map.get("userName"));
            user.setUserState(map.get("userState"));
            user.setDepartmentId(map.get("departmentId"));
            user.setUserRank(map.get("userRank"));
            user.setUserTeleno(map.get("userTeleno"));
            user.setUserDesc(map.get("userDesc"));
            //??????????????????
            userDAO.save(user);
            return new Message(true,"????????????");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

    /**
     * ????????????
     * @param imgFile
     * @param imgType
     * @return
     * */
    @Override
    public IMessage uploadHeadAvatar(String imgFile, String imgType){
        try {
            String path = environment.getProperty("spring.headImgPath")+"/";//#??????????????????
            String staticPath = environment.getProperty("spring.staticHeadImg")+"/";
            String name = UUID.randomUUID().toString().replaceAll("-","");
            String staticFullPath = "";
            Message message = new Message();
            staticFullPath = staticPath+name+imgType;
            User curUser = getCurrentUser();
            if(curUser == null){
                return new Message(false,"??????????????????");
            }
            User user = userDAO.findUserByUserNumber(getCurrentUser().getUserNumber());
            if(null == user){
                return new Message(false,"??????????????????");
            }
            boolean flag= FileUtils.writeImg(imgFile,imgType,name,path);
            if (flag) {
                if (ParamUtils.allNotNull(user.getHeadAvatar())) {//??????????????????????????????????????????
                    if (FileUtils.deleteImg(path, user.getHeadAvatar().replaceAll(staticPath, ""))) {
                        userDAO.updateUserHead(staticFullPath, getCurrentUser().getUserNumber());//??????????????????????????????
                    } else {
                        return new Message(false, "?????????????????????");
                    }
                } else {
                    userDAO.updateUserHead(staticFullPath, getCurrentUser().getUserNumber());
                }
                message.setMsg("????????????");
                message.setSuccess(true);
                message.setData(staticFullPath);
                return message;
            } else {
                message.setMsg("??????????????????");
                message.setSuccess(false);
                return message;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"????????????");
        }
    }

}
