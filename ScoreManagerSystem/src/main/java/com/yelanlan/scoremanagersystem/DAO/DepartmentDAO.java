package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentDAO extends JpaRepository<Department,String> {
    @Query("select d from Department d where d.id in (?1)")
    List<Department> findAllByIds(List<String> ids);

    @Query("select d from Department d where d.id = ?1")
    Department findDepartById(String id);

    //根据院id查询其下的系信息
    @Query("select d from Department d where  d.parentId = ?1")
    List<Department> findAllByParentId(String parentId);

    //根据院id删除其下的系信息
    @Transactional
    @Modifying
    @Query("delete from Department d where d.parentId = ?1")
    void  deleteAllByParentId(String parentId);

    //根据时间升序查询所有节点
    @Query("select d from Department d order by d.crtDate asc")
    List<Department> findAllAndOrderBTime();
}
