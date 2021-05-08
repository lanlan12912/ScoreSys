package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;

import java.util.Map;

public interface IActivityService {
    /**
     * 新增用户信息
     * @param map
     * @retun
     * */
    public IMessage crtActivity(Map<String,String> map);

    /**
     * 修改活动信息
     * @param  map
     * @return
     * */
    public IMessage updateActInfo(Map<String,String> map);

    /**
     * 分页查询所有活动
     * @parma map
     * @return
     * */
    public IMessage getAllActs(Map<String,Object> map,int start,int limit,int actFlag);

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
    public IMessage getJudgeAct(Map<String,String> map,int start,int limit);

    /**
     * 上传活动照片，保存路径
     * @param
     * @return
     * */
    public IMessage uploadImgs();
}
