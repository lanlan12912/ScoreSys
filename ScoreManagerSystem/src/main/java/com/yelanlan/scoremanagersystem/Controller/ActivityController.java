package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Enum.ActRankEnum;
import com.yelanlan.scoremanagersystem.Enum.ActStateEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.ServiceIface.IParticipateService;
import com.yelanlan.scoremanagersystem.ServiceIface.IUserService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ActivityController {
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IParticipateService participateService;
    @Autowired
    private IUserService userService;

    @RequestMapping("/getActRanks")
    public IMessage getActRanks(){
        try {
            Message message = new Message(true,"查询成功");
            List<Map<String,String>> ranks = new ArrayList<Map<String,String>>();
            for(ActRankEnum rank : ActRankEnum.values()){
                Map<String,String> map = new HashMap<>();
                map.put("code",rank.toString());
                map.put("name",rank.getName());
                ranks.add(map);
            }
            message.setData(ranks);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/saveActInfo")
    public IMessage saveActInfo(@RequestBody Map<String,String> map){
        User user = userService.getCurrentUser();
        if(null == user){
            return new Message(false,"请登陆后操作");
        }
        if(!ParamUtils.allNotNull(map.get("actName"),map.get("actRank"),map.get("startDate"),map.get("endDate"))){
            return new Message(false,"信息填写不完整");
        }
        if(!ParamUtils.allNotNull(map.get("id"))){
            return activityService.crtActivity(map,user);
        }else {
            return activityService.updateActInfo(map,user);
        }
    }

    @RequestMapping("/getAllActList")
    public IMessage getAllActList(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
            return new Message(false,"分页参数异常");
        }
        User user = userService.getCurrentUser();
        if(null == user){
            return new Message(false,"请登陆后操作");
        }
        int start = Integer.parseInt(String.valueOf(map.get("start")));
        int limit = Integer.parseInt(String.valueOf(map.get("limit")));
        if(!ParamUtils.allNotNull(map.get("myAct"))){
            return new Message(false,"参数异常");
        }
        int actFlag = Integer.parseInt(String.valueOf(map.get("myAct")));
        return activityService.getAllActs(map,start,limit,actFlag,user);
    }

    @RequestMapping("/delActInfo")
    public IMessage delActInfo(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"请指定活动方可删除");
        }
        return activityService.delActInfo(map.get("id"));
    }

    @RequestMapping("/getJudgeAct")
    public IMessage getJudgeAct(@RequestBody Map<String,String> map){
        User user = userService.getCurrentUser();
        if(null == user){
            return new Message(false,"请登陆后操作");
        }
        if(!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
            return new Message(false,"分页参数异常");
        }
        int start = Integer.parseInt(String.valueOf(map.get("start")));
        int limit = Integer.parseInt(String.valueOf(map.get("limit")));
        return activityService.getJudgeAct(map,start,limit,user);
    }

    @RequestMapping("/uploadImgs")
    public IMessage uploadImgs(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map.get("id"))) {
            return new Message(false, "参数异常");
        }
        if(!ParamUtils.allNotNull(map.get("imgFile"))){
            return new Message(false,"图片参数为空");
        }
        List<String> imgFile = (List<String>) map.get("imgFile");
        if(imgFile.size()>0){
            return  activityService.uploadImgs(imgFile,String.valueOf(map.get("id")));
        }else {
            return new Message(false,"没有可上传的图片");
        }

    }

    @RequestMapping("/delActImg")
    public IMessage delActImg(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"),map.get("actImg"))){
            return new Message(false,"参数异常");
        }
        return activityService.delActImg(map.get("id"),map.get("actImg"));
    }

    @RequestMapping("/getActInfo")
    public IMessage getActInfo(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"参数为空");
        }
        return activityService.getActInfo(map.get("id"));
    }

    @RequestMapping("/signAct")
    public IMessage signAct(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"活动信息参数异常");
        }
        User user = userService.getCurrentUser();
        if(null == user){
            return new Message(false,"请登陆后操作");
        }
        return participateService.signAct(map.get("id"),user);
    }

    @RequestMapping("/getPartInfo")
    public IMessage getPartInfo(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("id"))){
            return new Message(false,"活动信息参数异常");
        }
        User user = userService.getCurrentUser();
        if(null == user){
            return new Message(false,"请登陆后操作");
        }
        return participateService.getUserActPartInfo(map.get("id"),user);
    }

    @RequestMapping("/uploadCertImg")
    public IMessage uploadCertImg(@RequestBody Map<String,String> map){
        if (!ParamUtils.allNotNull(map.get("actId"),map.get("certImg"),map.get("partInState"))){
            return new Message(false,"参数异常");
        }
        User user = userService.getCurrentUser();
        if(user == null){
            return new Message(false,"请登录后操作");
        }
        String imgFile = map.get("certImg");
        String imgType = ".png";
        if (imgFile.substring(5, 14).equals("image/png")) {
            imgType = ".png";
        }
        if (imgFile.substring(5, 15).equals("image/jpeg") || imgFile.substring(5, 14).equals("image/jpg")) {
            imgType = ".jpg";
        }
        return participateService.uploadCertImg(imgFile,imgType,map.get("partInState"),map.get("actId"),user);
    }

    @RequestMapping("/judgeAct")
    public IMessage judgeAct(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("actId"),map.get("actJudge"))){
            return new Message(false,"参数异常");
        }
        User user = userService.getCurrentUser();
        if(user == null){
            return new Message(false,"请登陆后操作");
        }
        return activityService.judgeAct(map.get("actId"),map.get("actJudge"),user);
    }

    @RequestMapping("/getActScoreSort")
    public IMessage getActScoreSort(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("actId"))){
            return new Message(false,"参数异常");
        }
        return participateService.getActScoreSort(map.get("actId"));
    }

    @RequestMapping("/getPartList")
    public IMessage getPartList(@RequestBody Map<String,String> map){
        if (!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
            return new Message(false,"为输入分页参数");
        }
        int start = Integer.parseInt(map.get("start"));
        int limit = Integer.parseInt(map.get("limit"));
        if(!ParamUtils.allNotNull("actId")){
            return new Message(false,"参数异常");
        }
        String actId = map.get("actId");
        return participateService.getPartList(start,limit,actId,map);
    }

    @RequestMapping("/passCert")
    public IMessage passCert(@RequestBody Map<String,String> map){
        if(!ParamUtils.allNotNull(map.get("partId"),map.get("judgeFlag"))){
            return new Message(false,"参数异常");
        }
        if(ActStateEnum.valueOf(map.get("judgeFlag")) == null){
            return new Message(false,"参数异常");
        }
        return participateService.passCert(map.get("partId"),ActStateEnum.valueOf(map.get("judgeFlag")));
    }

    @RequestMapping("/getMyScoreInfo")
    public IMessage getMyScoreInfo(@RequestBody Map<String,String> map){
        try {
            User user = userService.getCurrentUser();
            if(null== user){
                return new Message(false,"请登陆后操作");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;
            if(ParamUtils.allNotNull(map.get("startDate"))){
                startDate = sdf.parse(map.get("startDate"));
            }
            if(ParamUtils.allNotNull(map.get("endDate"))){
                endDate = sdf.parse(map.get("endDate"));
            }
            return participateService.getMyScoreInfo(user,startDate,endDate);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(false,"系统异常");
        }
    }

    @RequestMapping("/getAllRank")
    public IMessage getAllRank(@RequestBody Map<String,String> map){
        return participateService.getAllRank(map);
    }

}
