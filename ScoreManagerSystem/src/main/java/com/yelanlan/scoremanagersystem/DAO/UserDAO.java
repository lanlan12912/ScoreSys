package com.yelanlan.scoremanagersystem.DAO;


import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDAO  extends JpaRepository<User,String> , JpaSpecificationExecutor<User> {
    //根据用户账号查找用户
    @Query("select u from User u where u.userNumber = :userNumber")
    User findUserByUserNumber(String userNumber);

    //根据用户账号修改用户密码
    @Modifying
    @Query("update User u set u.userPwd = ?1 where u.userNumber = ?2")
    int setFixedUserNumber(String userNumber, String userPwd);

    //根据院id查找用户
    @Query("select u from User u where u.collegeId = ?1 ")
    List<User> findAllByCollegeId(String collegeId);

    //根据系id查询用户
    @Query("select u from User u where u.departmentId = ?1")
    List<User> findAllByDepartmentId(String departmentId);

    //查找一个院/系的用户
    @Query("select u from User u where u.collegeId in (?1) or u.departmentId in (?2)")
    List<User> findAllByCollegeAndDepart(List<String> cIds, List<String> dIds);

}
