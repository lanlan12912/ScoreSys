package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.ActivityDAO;
import com.yelanlan.scoremanagersystem.DAO.ParticipateDAO;
import com.yelanlan.scoremanagersystem.Enum.ActStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Activity;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.ParticipateInfo;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityService implements IActivityService {
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private ParticipateDAO participateDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private Environment environment;

    /**
     * 新增活动信息
     * @param map
     * @retun
     * */
    @Override
    public IMessage crtActivity(Map<String,String> map){
        try {
            User user = userService.getCurrentUser();
            String id = UUID.randomUUID().toString().replaceAll("-","");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date satrtDate = sdf.parse(map.get("startDate"));
            Date endDate = sdf.parse(map.get("endDate"));
            Activity activity = new Activity(id,map.get("actName"),map.get("actDesc"),map.get("actSite"),
                    map.get("actHost"),map.get("actRank"),satrtDate,endDate,
                    user.getUserNumber(), DateUtils.getCurrentTime(),user.getUserNumber(),DateUtils.getCurrentTime());
            //初始化活动状态
            if(endDate.compareTo(new Date()) < 0 ){//结束日期小于当前日期，已结束
                activity.setActState(ActStateEnum.ENDED.toString());
            }else if (satrtDate.compareTo(new Date()) >0){//开始日期大于当前日期，未开始
                activity.setActState(ActStateEnum.NOTSTART.toString());
            }else {//进行中
                activity.setActState(ActStateEnum.ONGOING.toString());
            }
            //初始化删除字段为未删除
            activity.setDelFlag(0);
            //初始化审核状态为：申报中
            activity.setActJudge(ActStateEnum.INDECLARATION.toString());
            activityDAO.save(activity);
            return new Message(true,"已添加，等待审核");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 修改活动信息
     * @param  map
     * @return
     * */
    @Override
    public IMessage updateActInfo(Map<String,String> map){
        try {
            Activity activity = activityDAO.findAllById(map.get("id"));
            if(activity == null){
                return new Message(false,"找不到活动信息");
            }
            if(activity.getDelFlag()==1){
                return new Message(false,"活动已被删除");
            }
            //先将记录删除
            activityDAO.delete(activity);
            activity.setActName(map.get("actName"));
            activity.setActSite(map.get("actSite"));
            activity.setActHost(map.get("actHost"));
            activity.setActDesc(map.get("actDesc"));
            //再新建一条记录
            activityDAO.save(activity);
            return new Message(true,"已修改");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 分页查询所有活动
     * @arma map
     * @return
     * */
    @Override
    public IMessage getAllActs(Map<String,Object> map,int start,int limit,int actFlag){
        try {
            User currentUser = userService.getCurrentUser();
            Specification<Activity> spec = new Specification<Activity>() {
                @Override
                public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    //筛选未删除的活动信息
                    predicateList.add(criteriaBuilder.equal(root.get("delFlag"),0));
                    if(ParamUtils.allNotNull(map.get("actName"))){
                        predicateList.add(criteriaBuilder.like(root.get("actName"),String.valueOf(map.get("actName"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actHost"))){
                        predicateList.add(criteriaBuilder.like(root.get("actHost"),String.valueOf(map.get("actHost"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actState"))){
                        predicateList.add(criteriaBuilder.equal(root.get("actState"),String.valueOf(map.get("actState"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actRank"))){
                        predicateList.add(criteriaBuilder.equal(root.get("actRank"),String.valueOf(map.get("actRank"))));
                    }
                    switch (actFlag){
                        case 0://全部活动
                            //审核状态为通过
                            predicateList.add(criteriaBuilder.equal(root.get("actJudge"),ActStateEnum.PASS.toString()));
                            break;
                        case 1://我参与的
                            //获取用户的活动参与信息
                            List<ParticipateInfo> parts = participateDAO.findAllByUserNumber(currentUser.getUserNumber());
                            List<String> ids = parts.stream().map(ParticipateInfo::getActId).collect(Collectors.toList());
                            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("id"));
                            if(ids.size()>0) {
                                for(String s : ids){
                                    in.value(s);
                                }
                                predicateList.add(in);
                            }else{
                                in.value("");
                                predicateList.add(in);
                            }
                            break;
                        case 2://我发布的
                            predicateList.add(criteriaBuilder.equal(root.get("crtUser"),currentUser.getUserNumber()));
                            break;
                        default:
                            break;
                    }
                    return criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
                }
            };
            Page<Activity> actPage = activityDAO.findAll(spec,PageRequest.of(start-1,limit));
            Message message = new Message(true,"查询成功");
            message.setData(actPage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }


    /**
     * 删除活动
     * @param  id
     * @return
     * */
    @Override
    public IMessage delActInfo(String id){
        try {
            int delFlag = activityDAO.delActById(id);
            if(delFlag>0){
                return new Message(true,"删除成功");
            }else {
                return new Message(false,"删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 查询需要审核的活动
     * @param map
     * @param start
     * @param limit
     * @return
     * */
    @Override
    public IMessage getJudgeAct(Map<String,String> map,int start,int limit){
        try {
            User currentUser = userService.getCurrentUser();
            Specification<Activity> spec = new Specification<Activity>() {
                @Override
                public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    //筛选未删除的活动信息
                    predicateList.add(criteriaBuilder.equal(root.get("delFlag"),0));
                    //审核状态为通过
                    predicateList.add(criteriaBuilder.equal(root.get("actJudge"),ActStateEnum.INJUDGE.toString()));
                    if(ParamUtils.allNotNull(map.get("actName"))){
                        predicateList.add(criteriaBuilder.like(root.get("actName"),String.valueOf(map.get("actName"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actHost"))){
                        predicateList.add(criteriaBuilder.like(root.get("actHost"),String.valueOf(map.get("actHost"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actState"))){
                        predicateList.add(criteriaBuilder.equal(root.get("actState"),String.valueOf(map.get("actState"))));
                    }
                    if(ParamUtils.allNotNull(map.get("actRank"))){
                        predicateList.add(criteriaBuilder.equal(root.get("actRank"),String.valueOf(map.get("actRank"))));
                    }
                    return criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
                }
            };
            Page<Activity> actPage = activityDAO.findAll(spec,PageRequest.of(start-1,limit));
            Message message = new Message(true,"查询成功");
            message.setData(actPage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 上传活动照片，保存路径
     * @param
     * @return
     * */
    @Override
    public IMessage uploadImgs(){
        try {



            Message message = new Message(true,"已上传");
            message.setData("");
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
