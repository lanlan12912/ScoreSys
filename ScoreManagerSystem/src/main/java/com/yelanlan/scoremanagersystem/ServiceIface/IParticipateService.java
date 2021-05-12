package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;
import com.yelanlan.scoremanagersystem.RepositoryImpl.User;

import java.util.Map;

public interface IParticipateService {

    /**
     * 报名活动
     * @param actId
     * @param currentUser
     * @return
     * */
    public IMessage signAct(String actId, User currentUser);

    /**
     * 上传证明材料
     * @param imgFile
     * @param imgType
     * @param partInState
     * @param actId
     * @param user
     * @return
     * */
    public IMessage uploadCertImg(String imgFile, String imgType, String partInState, String actId,User user);

    /**
     * 查询当前用户对某条活动的参与信息
     * @param actId
     * @param user
     * @return
     * */
    public IMessage getUserActPartInfo(String actId,User user);

    /**
     *获取活动的成绩排行
     * @parame actId
     * @param
     * @return
     * */
    public IMessage getActScoreSort(String actId);

    /**
     * 获取参与信息的审核材料
     * @param start
     * @param limit
     * @param actId
     * @param filter
     * @return
     * */
    public IMessage getPartList(int start, int limit, String actId, Map<String,String> filter);
}
