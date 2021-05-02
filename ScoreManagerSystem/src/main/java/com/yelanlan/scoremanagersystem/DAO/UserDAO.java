package com.yelanlan.scoremanagersystem.DAO;


import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDAO  extends JpaRepository<User,String> , JpaSpecificationExecutor<User> {
    //根据用户账号查找用户
    @Query("select u from User u where u.userNumber = :userNumber")
    User findUserByUserNumber(String userNumber);

    //根据用户账号修改用户密码
    @Modifying
    @Query("update User u set u.userPwd = ?1 where u.userNumber = ?2")
    int setFixedUserNumber(String userNumber, String userPwd);

    //根据一个院/系id查询用户
    @Query("select u from User u where u.departmentId = ?1 order by u.userNumber asc")
    List<User> findAllByDepartmentId(String departmentId);

    //查找多个个院/系的用户
    @Query("select u from User u where u.departmentId in (?1) order by u.userNumber asc")
    List<User> findAllByCollegeAndDepart(List<String> ids);

    @Transactional
    @Modifying
    @Query("delete from User u where u.userNumber in (?1)")
    void deleteAllByUserNumbers(List<String> ids);

}
