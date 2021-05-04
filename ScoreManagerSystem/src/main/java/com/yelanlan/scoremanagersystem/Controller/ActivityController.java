package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Enum.ActRankEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.Common.Message;
import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.ServiceIface.IParticipateService;
import com.yelanlan.scoremanagersystem.Utils.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IParticipateService participateService;

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
        if(!ParamUtils.allNotNull(map.get("actName"),map.get("actRank"),map.get("actHost"),
                map.get("startDate"),map.get("endDate"),map.get("actSite"))){
            return new Message(false,"信息填写不完整");
        }
        if(!ParamUtils.allNotNull(map.get("id"))){
            return activityService.crtActivity(map);
        }else {
            return activityService.updateActInfo(map);
        }
    }

    @RequestMapping("/getAllActList")
    public IMessage getAllActList(@RequestBody Map<String,Object> map){
        if(!ParamUtils.allNotNull(map.get("start"),map.get("limit"))){
            return new Message(false,"分页参数异常");
        }
        int start = Integer.parseInt(String.valueOf(map.get("start")));
        int limit = Integer.parseInt(String.valueOf(map.get("limit")));
        return activityService.getAllActs(map,start,limit);
    }
}
