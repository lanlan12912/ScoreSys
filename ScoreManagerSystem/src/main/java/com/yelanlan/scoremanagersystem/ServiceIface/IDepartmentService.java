package com.yelanlan.scoremanagersystem.ServiceIface;

import com.yelanlan.scoremanagersystem.Enum.DepartEnum;
import com.yelanlan.scoremanagersystem.RepositoryIface.Common.IMessage;

import java.util.Map;

public interface IDepartmentService {
    /**
     * 更新院系信息
     * @param map
     * @return
     * */
    public IMessage updateDepart(Map<String,String> map);

    /**
     * 新增院系信息
     * @param map
     * @return
     * */
    public IMessage saveDepart(Map<String,String> map);

    /**
     * 删除院系信息
     * @param id
     * @return
     * */
    public IMessage delDepartment(String id);

    /**
     * 查询院系树
     * @param type
     * @return
     * */
    public IMessage quDepartTree(DepartEnum type);

    /**
     * 根据id查询院系信息
     * @param id
     * @return
     * */
    public IMessage quDepartById(String id);

}
