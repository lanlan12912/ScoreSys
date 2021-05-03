package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.RoleRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleResDAO extends JpaRepository<RoleRes,String> {
    @Query("select  r from RoleRes  r where r.relId = ?1")
    RoleRes findByRelId(String relId);

    @Query("select r from RoleRes  r where r.roleId = ?1")
    List<RoleRes> findAllByRoleId(String roleId);

    @Query("select r from  RoleRes  r where r.resId = ?1")
    List<RoleRes> findAllByResId(String resId);

    @Query("select r from RoleRes r where r.resId in (?1)")
    List<RoleRes> findAllByResIds(List<String> resIds);

    @Modifying
    @Transactional
    @Query("delete from RoleRes  r where r.roleId = ?1")
    void deleteAllByRoleId(String roleId);

    @Modifying
    @Transactional
    @Query("delete from RoleRes  r where r.roleId in (?1)")
    void deleteAllByRoleIds(List<String> roleIds);

    @Modifying
    @Transactional
    @Query("delete from RoleRes  r where r.resId in (?1)")
    void deleteAllByRoesIds(List<String> resIds);
}

 