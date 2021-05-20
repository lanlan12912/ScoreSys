package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

import java.util.List;
import java.util.Map;

public interface IActivityService {
    /**
     * 新增用户信息
     * @param map
     * @param user
     * @retun
     * */
    public IMessage crtActivity(Map<String,String> map,User user);

    /**
     * 修改活动信息
     * @param  map
     * @param  user
     * @return
     * */
    public IMessage updateActInfo(Map<String,String> map,User user);

    /**
     * 分页查询所有活动
     * @parma map
     * @return
     * */
    public IMessage getAllActs(Map<String,Object> map, int start, int limit, int actFlag, User currentUser);

    /**
     * 删除活动
     * @param  id
     * @return
     * */
    public IMessage delActInfo(String id);

    /**
     * 查询需要审核的活动
     * @param map
     * @param start
     * @param limit
     * @return
     * */
    public IMessage getJudgeAct(Map<String,String> map,int start,int limit,User currentUser );

    /**
     * 上传活动照片，保存路径
     * @param imgFile
     * @param id
     * @return
     * */
    public IMessage uploadImgs(List<String> imgFile,String id);

    /**
     * 删除活动照片
     * @param id
     * @param actImg
     * @return
     * */
    public IMessage delActImg(String id,String actImg);

    /**
     * 获取活动信息
     * @param id
     * @return
     * */
    public IMessage getActInfo(String id);

    /**
     * 审核活动信息是否可以发布到使用
     * @param actId
     * @param actJudge
     * @param user
     * @return
     * */
    public IMessage judgeAct(String actId,String actJudge,User user);
}
