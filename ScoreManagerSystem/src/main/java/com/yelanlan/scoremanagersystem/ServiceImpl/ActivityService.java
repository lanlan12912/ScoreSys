package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.ActivityDAO;
import com.yelanlan.scoremanagersystem.DAO.ParticipateDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.ActRankEnum;
import com.yelanlan.scoremanagersystem.Enum.ActStateEnum;
import com.yelanlan.scoremanagersystem.Enum.PartInEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Activity;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.MScoreDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.ParticipateInfo;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.Utils.DateUtils;
import com.yelanlan.scoremanagersystem.Utils.FileUtils;
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
    private UserDAO userDao;
    @Autowired
    private Environment environment;

    /**
     * 新增活动信息
     * @param map
     * @retun
     * */
    @Override
    public IMessage crtActivity(Map<String,String> map,User user){
        try {
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
            Activity activity = activityDAO.findAllById(map.get("id"));
            if(activity == null){
                return new Message(false,"找不到活动信息");
            }
            if(activity.getDelFlag()==1){
                return new Message(false,"活动已被删除");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date satrtDate = sdf.parse(map.get("startDate"));
            Date endDate = sdf.parse(map.get("endDate"));
            //更新活动状态
            if(endDate.compareTo(new Date()) < 0 ){//结束日期小于当前日期，已结束
                activity.setActState(ActStateEnum.ENDED.toString());
            }else if (satrtDate.compareTo(new Date()) >0){//开始日期大于当前日期，未开始
                activity.setActState(ActStateEnum.NOTSTART.toString());
            }else {//进行中
                activity.setActState(ActStateEnum.ONGOING.toString());
            }
            //先将记录删除
            activityDAO.delete(activity);
            activity.setActName(map.get("actName"));
            activity.setStartDate(satrtDate);
            activity.setEndDate(endDate);
            activity.setActSite(map.get("actSite"));
            activity.setActHost(map.get("actHost"));
            activity.setActDesc(map.get("actDesc"));
            //修改后重新审核
            activity.setActJudge(ActStateEnum.INJUDGE.toString());
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
    public IMessage getAllActs(Map<String,Object> map,int start,int limit,int actFlag,User currentUser){
        try {
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
                        Date now = new Date();
                        switch (ActStateEnum.valueOf(String.valueOf(map.get("actState")))){
                            case NOTSTART:
                                predicateList.add(criteriaBuilder.lessThan(root.get("startDate"),now));
                                break;
                            case ONGOING:
                                predicateList.add(criteriaBuilder.greaterThan(root.get("startDate"),now));
                                predicateList.add(criteriaBuilder.lessThan(root.get("endDate"),now));
                                break;
                            case ENDED:
                                predicateList.add(criteriaBuilder.greaterThan(root.get("endDate"),now));
                                break;
                            default:
                                break;
                        }

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
    public IMessage getJudgeAct(Map<String,String> map,int start,int limit,User currentUser ){
        try {
            Specification<Activity> spec = new Specification<Activity>() {
                @Override
                public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    //筛选未删除的活动信息
                    predicateList.add(criteriaBuilder.equal(root.get("delFlag"),0));
                    //审核状态为审核中
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
     * @param imgFiles
     * @param id
     * @return
     * */
    @Override
    public IMessage uploadImgs(List<String> imgFiles, String id){
        try {
            String path = environment.getProperty("spring.actImgPath")+"/";//#活动照片路径
            String staticPath = environment.getProperty("spring.staticActImg")+"/";
            boolean flag = false;
            Activity activity = activityDAO.findAllById(id);
            if (activity == null) {
                return new Message(false, "找不到活动");
            }
            List<String> list = null;//将原来的图片转成列表
            if (ParamUtils.allNotNull(activity.getActImgs())) {
                list = Arrays.asList(activity.getActImgs().split(";"));
            }
            List<String> imgs = list == null ? new ArrayList<>() : new ArrayList<>(list);
            Message message = new Message();
            for (String file : imgFiles) {
                String name = UUID.randomUUID().toString().replaceAll("-","");
                String imgType = ".jpg";//确定文件类型
                if (file.substring(5, 14).equals("image/png")) {
                    imgType = ".png";
                }
                if (file.substring(5, 15).equals("image/jpeg") || file.substring(5, 14).equals("image/jpg")) {
                    imgType = ".jpg";
                }
                String staticFullPath = staticPath + name + imgType;//前端展示路径
                flag = FileUtils.writeImg(file, imgType, name, path);//保存图片
                if (flag) {//保存成功
                    imgs.add(staticFullPath);
                    message.setMsg("上传成功");
                    message.setSuccess(true);
                    message.setData(imgs);
                    continue;
                } else {//保存失败
                    message.setMsg("系统异常，上传失败");
                    message.setSuccess(false);
                    break;
                }
            }
            activityDAO.updateActImgs(String.join(";", imgs), id);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 删除活动照片
     * @param id
     * @param actImg
     * @return
     * */
    @Override
    public IMessage delActImg(String id,String actImg){
        try {
            Activity activity = activityDAO.findAllById(id);
            if(activity==null){
                return new Message(false,"活动信息已找不到");
            }
            String path = environment.getProperty("spring.actImgPath")+"/";//boot中配置文件配置附件路径
            String fileName = actImg.replaceAll(environment.getProperty("spring.staticActImg")+"/","");
            List<String> imgs = Arrays.asList(activity.getActImgs().replaceAll(actImg, "").split(";"));
            if(FileUtils.deleteImg(path,fileName)){//文件删除成功，更新数据库
                imgs = imgs.stream().filter(item -> !item.equals(actImg)).collect(Collectors.toList());
                activityDAO.updateActImgs(String.join(";", imgs),id);
                Message message = new Message(true,"删除成功");
                message.setData(imgs);
                return message;
            }else {//失败
                return new Message(false,"删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 获取活动信息
     * @param id
     * @return
     * */
    @Override
    public IMessage getActInfo(String id){
        try {
            Activity activity = activityDAO.findAllById(id);
            Map<String,Object> map = new HashMap<>();
            //获取活动的等级获奖分数
            ActRankEnum rankEnum = ActRankEnum.valueOf(activity.getActRank());
            //更新活动状态
            if(activity.getEndDate().compareTo(new Date()) < 0 ){//结束日期小于当前日期，已结束
                activity.setActState(ActStateEnum.ENDED.toString());
            }else if (activity.getStartDate().compareTo(new Date()) >0){//开始日期大于当前日期，未开始
                activity.setActState(ActStateEnum.NOTSTART.toString());
            }else {//进行中
                activity.setActState(ActStateEnum.ONGOING.toString());
            }
            List<MScoreDTO> scoreDTOS = new ArrayList<>();
            for (PartInEnum o : PartInEnum.values()) {
                switch (o){
                    case FPRISE://一等奖
                        scoreDTOS.add(new MScoreDTO(rankEnum.getName(),o.toString(),o.getName(),rankEnum.getPriseScore1()));
                        break;
                    case SPRISE://二等奖
                        scoreDTOS.add(new MScoreDTO(rankEnum.getName(),o.toString(),o.getName(),rankEnum.getPriseScore2()));
                        break;
                    case TPRISE://三等奖
                        scoreDTOS.add(new MScoreDTO(rankEnum.getName(),o.toString(),o.getName(),rankEnum.getPriseScore3()));
                        break;
                    case OPRISE://其他获奖
                        scoreDTOS.add(new MScoreDTO(rankEnum.getName(),o.toString(),o.getName(),rankEnum.getoPriseScore()));
                        break;
                    case PARTINED://参与
                        scoreDTOS.add(new MScoreDTO(rankEnum.getName(),o.toString(),o.getName(),rankEnum.getPartScore()));
                        break;
                    default:
                        break;
                }
            }
            //分数降序
            scoreDTOS = scoreDTOS.stream().sorted(Comparator.comparing(MScoreDTO::getValue).reversed()).collect(Collectors.toList());
            //格式化活动级别
            activity.setActRank(ActRankEnum.valueOf(activity.getActRank()).getName());
            map.put("activity",activity);
            map.put("scoreList",scoreDTOS);
            Message message = new Message(true,"查询成功");
            message.setData(map);
            return message;
        }catch (Exception e){
            return new Message(false,"系统异常");
        }
    }

    /**
     * 审核活动信息是否可以发布到使用
     * @param actId
     * @param actJudge
     * @param user
     * @return
     * */
    @Override
    public IMessage judgeAct(String actId,String actJudge,User user){
        try {
            Activity activity = activityDAO.findAllById(actId);
            if(null == activity){
                return new Message(false,"活动信息已不存在");
            }
            if(activity.getDelFlag() == 1){
                return new Message(false,"该活动已经被删除,无法操作");
            }
            activityDAO.delete(activity);
            activity.setActJudge(ActStateEnum.valueOf(actJudge).toString());//更新审核状态
            activity.setJudegUser(user.getUserNumber());
            activity.setJudgeDate(DateUtils.getCurrentTime());
            activityDAO.save(activity);
            Message message = new Message(true,"已审核");
            return message;
        }catch (Exception e) {
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }
}
