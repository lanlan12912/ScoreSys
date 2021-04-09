package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleDao extends JpaRepository<Role,String> {
    @Query("select new com.yelanlan.scoremanagersystem.RepositoryImpl.Role(r.roleId,r.roleName,u.userName,r.crtDate,r.roleDes) from Role r,User u where r.crtUser = u.userNumber order by r.crtDate desc")
    Page<Role> findAllTUserName(PageRequest pageRequest);

    @Transactional//支持事务
    @Modifying
    @Query("delete from Role r where r.roleId in (?1)")
    void  deleteAllByRoleId(List<String> roles);

    @Transactional
    @Modifying
    @Query("update Role r set r.roleName = ?1,r.roleDes = ?2 where r.roleId = ?3")
    int updateRole(String roleName,String roleDes,String roleId);

}
