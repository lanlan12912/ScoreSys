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
     * @Rarma map
     * @return
     * */
    public IMessage getAllActs(Map<String,Object> map,int start,int limit);
}
