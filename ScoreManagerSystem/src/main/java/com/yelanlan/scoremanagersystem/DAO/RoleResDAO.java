package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.RoleRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleResDAO extends JpaRepository<RoleRes,String> {
    @Query("select  r from RoleRes  r where r.relId = ?1")
    RoleRes findByRelId(String relId);

    @Query("select r from RoleRes  r where r.roleId = ?1")
    List<RoleRes> findAllByRoleId(String roleId);

    @Query("select r from  RoleRes  r where r.resId = ?1")
    List<RoleRes> findAllByResId(String resId);
}
