package com.yelanlan.scoremanagersystem.ServiceImpl;

import com.yelanlan.scoremanagersystem.DAO.ActivityDAO;
import com.yelanlan.scoremanagersystem.DAO.DepartmentDAO;
import com.yelanlan.scoremanagersystem.DAO.ParticipateDAO;
import com.yelanlan.scoremanagersystem.DAO.UserDAO;
import com.yelanlan.scoremanagersystem.Enum.ActRankEnum;
import com.yelanlan.scoremanagersystem.Enum.ActStateEnum;
import com.yelanlan.scoremanagersystem.Enum.PartInEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Activity;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.ActPartDetailDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.CertInfoDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.ScoreInfoDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.UserScoreDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import com.yelanlan.scoremanagersystem.RepositoryImpl.ParticipateInfo;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IParticipateService;
import com.yelanlan.scoremanagersystem.Utils.FileUtils;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipateService implements IParticipateService {
    @Autowired
    private Environment environment;
    @Autowired
    private ParticipateDAO participateDAO;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DepartmentDAO departmentDAO;

    /**
     * 上传证明材料
     * @param imgFile
     * @param imgType
     * @param partInState
     * @param actId
     * @param user
     * @return
     * */
    @Override
    public IMessage uploadCertImg(String imgFile, String imgType, String partInState,String actId,User user){
        try {
            String path = environment.getProperty("spring.certImgPath")+"/";//证明材料路径
            String staticPath = environment.getProperty("spring.staticCertImg")+"/";
            String name = UUID.randomUUID().toString().replaceAll("-","");
            String staticFullPath = staticPath+name+imgType;
            Message message = new Message();
            //先根据当前用户与活动id获取到参与信息
            ParticipateInfo participateInfo = participateDAO.findAllByUserNumberAndActId(user.getUserNumber(),actId);
            if(null == participateInfo){
                return new Message(false,"您还没有参与该活动");
            }
            boolean flag = FileUtils.writeImg(imgFile,imgType,name,path);
            if(flag){
                if(ParamUtils.allNotNull(participateInfo.getCertImg())) {//更换证明前需将之后的图片删除
                    if (FileUtils.deleteImg(path, participateInfo.getCertImg().replaceAll(staticPath, ""))) {
                        participateDAO.delete(participateInfo);
                        participateInfo.setCertImg(staticFullPath);//证明材料路径
                        participateInfo.setPartInState(partInState);//获奖级别（参与/一获奖/二获奖/三获奖/其他获奖））
                        participateInfo.setCertState(ActStateEnum.INJUDGE.toString());//材料上传，状态即为审核中
                        participateDAO.save(participateInfo);//删除成功后跟新数据库
                    }else {
                        return new Message(false,"删除原图片失败");
                    }
                }else {
                    participateDAO.delete(participateInfo);
                    participateInfo.setCertImg(staticFullPath);//证明材料路径
                    participateInfo.setPartInState(partInState);//获奖级别（参与/一获奖/二获奖/三获奖/其他获奖））
                    participateInfo.setCertState(ActStateEnum.INJUDGE.toString());//材料上传，状态即为审核中
                    participateDAO.save(participateInfo);//直接更新数据库
                }
                message.setMsg("上传成功，等待审核");
                message.setSuccess(true);
                message.setData(participateInfo);
                return message;
            }else {
                message.setMsg("文件保存失败");
                message.setSuccess(false);
                return message;
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 报名活动
     * @param actId
     * @param currentUser
     * @return
     * */
    @Override
    public IMessage signAct(String actId,User currentUser){
        try {
            //添加参与记录，状态为报名
            ParticipateInfo participateInfo = new ParticipateInfo();
            String id = UUID.randomUUID().toString().replaceAll("-","");
            Activity activity = activityDAO.findAllById(actId);
            if(null == activity){
                return new Message(false,"活动信息不存在，无法报名");
            }
            if(activity.getDelFlag() == 1){
                return new Message(false,"活动已被删除，无法报名");
            }
            if(participateDAO.findAllByUserNumberAndActId(currentUser.getUserNumber(),actId)!=null){
                return new Message(false,"您已报名,不可重复报名");
            }
            participateInfo.setId(id);
            participateInfo.setActId(actId);
            participateInfo.setActRank(activity.getActRank());
            participateInfo.setUserNumber(currentUser.getUserNumber());
            participateInfo.setPartInState(PartInEnum.SIGNED.toString());
            participateInfo.setMeasureScore(0);//报名时默认得分为0分
            participateDAO.save(participateInfo);
            return new Message(true,"您已报名，请再规定时间内参与活动");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

   /**
    * 查询当前用户对某条活动的参与信息
    * @param actId
    * @param user
    * @return
    * */
   @Override
   public IMessage getUserActPartInfo(String actId,User user){
       try {
           ParticipateInfo participateInfo = participateDAO.findAllByUserNumberAndActId(user.getUserNumber(),actId);
//           if(participateInfo == null){
//               return new Message(false,"没有您的报名信息");
//           }
           Message message = new Message(true,"查询成功");
           message.setData(participateInfo);
           return message;
       }catch (Exception e){
           e.printStackTrace();
           return new Message(false,"系统异常");
       }
   }

   /**
    *获取活动的成绩排行
    * @parame actId
    * @param
    * @return
    * */
   @Override
   public IMessage getActScoreSort(String actId){
       try {
           Activity activity = activityDAO.findAllById(actId);
           if(activity==null){
               return new Message(false,"找不到活动信息");
           }
           if(activity.getDelFlag()==1){
               return new Message(false,"该活动已经被删除");
           }
           List<UserScoreDTO> scoreDTOS = participateDAO.getActScoreSort(actId);
           Message message = new Message(true,"查询成功");
           message.setData(scoreDTOS);
           return message;
       }catch (Exception e){
           e.printStackTrace();
           return new Message(false,"系统异常");
       }

   }

    /**
     * 获取参与信息的审核材料
     * @param start
     * @param limit
     * @param actId
     * @param filter
     * @return
     * */
    @Override
    public IMessage getPartList(int start, int limit, String actId, Map<String,String> filter){
        try {
            Activity activity = activityDAO.findAllById(actId);
            if(activity == null){
                return new Message(false,"未找到活动信息");
            }
            Specification<ParticipateInfo> spec = new Specification<ParticipateInfo>() {
                @Override
                public Predicate toPredicate(Root<ParticipateInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<>();
                    Path score = root.get("measureScore");
                    //筛选某条活动的参与信息
                    predicateList.add(criteriaBuilder.equal(root.get("actId"),filter.get("actId")));
                    //筛选符合参与状态不为空
                    predicateList.add(criteriaBuilder.notEqual(root.get("partInState"),criteriaBuilder.nullLiteral(String.class)));
                    predicateList.add(criteriaBuilder.notEqual(root.get("partInState"),""));
                    //也不为已报名的
                    predicateList.add(criteriaBuilder.notEqual(root.get("partInState"),PartInEnum.SIGNED.toString()));
                    if(ParamUtils.allNotNull(filter.get("userNumber"))){
                        predicateList.add(criteriaBuilder.equal(root.get("userNumber"),String.valueOf(filter.get("userNumber"))));
                    }
                    if(ParamUtils.allNotNull(filter.get("certState"))){
                        predicateList.add(criteriaBuilder.equal(root.get("certState"),String.valueOf(filter.get("certState"))));
                    }
                    if(ParamUtils.allNotNull(filter.get("departIds"))) {
                        CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("userNumber"));
                        List<String> departIds = Arrays.asList(String.valueOf(filter.get("departIds")).split(","));
                        if(departIds.size()>0) {
                            List<String> userNums = userDAO.findAllByCollegeAndDepart(departIds).stream().map(User::getUserNumber).collect(Collectors.toList());
                            if (userNums.size() > 0) {
                                for (String s : userNums) {
                                    in.value(s);
                                }
                                predicateList.add(in);
                            }
                        }
                    }
                    return criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()])).orderBy(criteriaBuilder.desc(score)).getRestriction();
                }
            };
            Page<ParticipateInfo> partPage = participateDAO.findAll(spec, PageRequest.of(start-1,limit));
            List<ParticipateInfo> infos = partPage.getContent();
            List<CertInfoDTO> infoDTOS = new ArrayList<>();
            if(infos.size()>0){
                for(ParticipateInfo  obj: infos){
                    CertInfoDTO dto = new CertInfoDTO(obj.getId(),obj.getUserNumber(),obj.getActRank(),
                            obj.getPartInState(),obj.getMeasureScore(),obj.getCertImg());
                    User user = userDAO.findUserByUserNumber(obj.getUserNumber());
                    dto.setActRank(ActRankEnum.valueOf(obj.getActRank()).getName());//转换活动等级
                    dto.setPartInState(PartInEnum.valueOf(obj.getPartInState()).getName());//转换获奖等级
                    dto.setUserName(user.getUserName());
                    Department depart = departmentDAO.findDepartById(user.getDepartmentId());
                    dto.setDepartName(depart.getDepartName());
                    Activity act = activityDAO.findAllById(obj.getActId());
                    dto.setActName(act.getActName());
                    dto.setCertState(ActStateEnum.valueOf(obj.getCertState()).getName());//转换审核状态
                    infoDTOS.add(dto);
                }
            }
            Page<CertInfoDTO> DTOPage = new PageImpl(infoDTOS,PageRequest.of(start-1,limit),partPage.getTotalElements());
            Message message = new Message(true,"查询成功");
            message.setData(DTOPage);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 审核上传的证明材料
     * @param partId
     * @param certState
     * @return
     * */
    public IMessage passCert(String partId,ActStateEnum certState){
        try {
            ParticipateInfo participateInfo = participateDAO.findAllById(partId);
            if(participateInfo == null){
                return new Message(false,"参与记录已经不存在");
            }
            double score =0;
            if(certState == ActStateEnum.PASS){
                switch (PartInEnum.valueOf(participateInfo.getPartInState())){
                    case PARTINED:
                        score = ActRankEnum.valueOf(participateInfo.getActRank()).getPartScore();
                        break;
                    case TPRISE:
                        score = ActRankEnum.valueOf(participateInfo.getActRank()).getPriseScore3();
                        break;
                    case SPRISE:
                        score = ActRankEnum.valueOf(participateInfo.getActRank()).getPriseScore2();
                        break;
                    case OPRISE:
                        score = ActRankEnum.valueOf(participateInfo.getActRank()).getoPriseScore();
                        break;
                    case FPRISE:
                        score = ActRankEnum.valueOf(participateInfo.getActRank()).getPriseScore1();
                        break;
                    default:
                        break;
                }
            }
            participateDAO.addPartScore(score,certState.toString(),partId);
            return new Message(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 获取当前用户的成绩信息
     * @param user
     * @param startDate
     * @param endDate
     */
    @Override
    public IMessage getMyScoreInfo(User user,Date startDate,Date endDate ){
        try {
            ScoreInfoDTO scoreInfoDTO = new ScoreInfoDTO();
            scoreInfoDTO.setUserNumber(user.getUserNumber());
            scoreInfoDTO.setUserName(user.getUserName());
            scoreInfoDTO.setTotalScore(0);
            scoreInfoDTO.setSignEdAct(0);
            scoreInfoDTO.setPartEdAct(0);
            scoreInfoDTO.setPartEdAct(0);
            scoreInfoDTO.setRanking("0");
            Department department = departmentDAO.findDepartById(user.getDepartmentId());
            if(null!=department){
                scoreInfoDTO.setDepartName(department.getDepartName());
            }
            List<String> actIDs = new ArrayList<>();
            //获取总分
            Object[] objTotal = null;
            List<ParticipateInfo> participateInfos = new ArrayList<>();
            List<Object[]> ranklist = new ArrayList<>();
            if(null!=startDate && null !=endDate){
                List<Activity> activities = activityDAO.findAllByDate(startDate,endDate);
                actIDs = activities.stream().map(Activity::getId).collect(Collectors.toList());
                if(actIDs.size()>0) {
                    //获取总分
                    objTotal = participateDAO.getUserTotalScore(user.getUserNumber(),actIDs).get(0);
                    participateInfos = participateDAO.getUserSignAct(user.getUserNumber(),actIDs,PartInEnum.SIGNED.toString());
                    //获取排名
                    ranklist=participateDAO.getUserRank(actIDs);
                }else {
                    //获取总分
                    objTotal = participateDAO.getUserTotalScore(user.getUserNumber()).get(0);
                    participateInfos = participateDAO.getUserSignAct(user.getUserNumber(),PartInEnum.SIGNED.toString());
                    //获取排名
                    ranklist=participateDAO.getUserRank();
                }
            }else {
                //获取总分
                objTotal = participateDAO.getUserTotalScore(user.getUserNumber()).get(0);
                participateInfos = participateDAO.getUserSignAct(user.getUserNumber(),PartInEnum.SIGNED.toString());
                //获取排名
                ranklist=participateDAO.getUserRank();
            }
            scoreInfoDTO.setTotalScore(objTotal==null?0:(objTotal[0] == null?0:Double.parseDouble(String.valueOf(objTotal[0]))));
            if(participateInfos.size()>0){
                //获取报名条数
                scoreInfoDTO.setSignEdAct(participateInfos.size());
                //获取参与条数
                scoreInfoDTO.setPartEdAct(participateInfos.stream().filter(item->item.getPartInState()==PartInEnum.PARTINED.toString())
                        .collect(Collectors.toList()).size());
                //获取获奖的条数
                scoreInfoDTO.setPirseAct(participateInfos.stream().filter(item->item.getPartInState()!=PartInEnum.PARTINED.toString()
                        &&item.getPartInState()!=PartInEnum.SIGNED.toString()).collect(Collectors.toList()).size());
            }
            if(ranklist.size()>0){
                for(Object[] objects : ranklist){
                    if(user.getUserNumber().equals(String.valueOf(objects[0]))){
                        scoreInfoDTO.setRanking((ranklist.indexOf(objects)+1)+"/"+ranklist.size());
                        break;
                    }
                }
            }
            Map<String,Object> res=  new HashMap<>();
            res.put("scoreInfo",scoreInfoDTO);
            //处理详情列表
            List<ActPartDetailDTO> scoreInfoDTOS = new ArrayList<>();
            for(ParticipateInfo info : participateInfos){
                ActPartDetailDTO detailDTO = new ActPartDetailDTO(info.getId(),ActRankEnum.valueOf(info.getActRank()).getName(),
                        PartInEnum.valueOf(info.getPartInState()).getName(),info.getCertImg(),info.getMeasureScore(),
                        ActStateEnum.valueOf(info.getCertState()).getName());
                Activity act = activityDAO.findAllById(info.getActId());
                detailDTO.setActName(act.getActName());
                detailDTO.setActDate(act.getStartDate()+"-"+act.getEndDate());
                scoreInfoDTOS.add(detailDTO);
            }
            res.put("scoreDetails",scoreInfoDTOS);
            Message message = new Message(true,"查询成功");
            message.setData(res);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    /**
     * 获取排行榜
     * @param map
     * @return
     * */
    @Override
    public IMessage getAllRank(Map<String,String> map){
        try {
            List<UserScoreDTO> scoreDTOS = new ArrayList<>();
            if(ParamUtils.allNotNull(map.get("departId"))){
                List<User> users = userDAO.findAllByDepartmentId(map.get("departId"));
                List<String> userIds = users.stream().map(User::getUserNumber).collect(Collectors.toList());
                scoreDTOS = participateDAO.getAllByUsers(userIds);
            }else {
                scoreDTOS = participateDAO.getAll();
            }
            Message message = new Message(true,"查询成功");
            message.setData(scoreDTOS);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

}
