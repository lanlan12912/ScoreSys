package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<Role,String> {
    @Query("select new com.yelanlan.scoremanagersystem.RepositoryImpl.Role(r.roleId,r.roleName,u.userName,r.crtDate,r.roleDes) from Role r,User u where r.crtUser = u.userNumber order by r.crtDate desc")
    Page<Role> findAllTUserName(PageRequest pageRequest);

}
