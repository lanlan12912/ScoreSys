package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.ActivityDAO;
import com.yelanlan.scoremanagersystem.Enum.ActStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Activity;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class ActivityService implements IActivityService {
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private UserService userService;

    /**
     * 新增用户信息
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
            //初始化审核状态为：审核中
            activity.setActJudge(ActStateEnum.INJUDGE.toString());
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

            return new Message(true,"已修改");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 分页查询所有活动
     * @Rarma map
     * @return
     * */
    @Override
    public IMessage getAllActs(Map<String,Object> map,int start,int limit){
        try {
            Specification<Activity> spec = new Specification<Activity>() {
                @Override
                public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
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

}
