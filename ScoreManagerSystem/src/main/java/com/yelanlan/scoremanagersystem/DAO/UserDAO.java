package com.yelanlan.scoremanagersystem.DAO;


import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDAO  extends JpaRepository<User,String> {
    //根据用户账号查找用户
    @Query("select u from User u where u.userNumber = :userNumber")
    User findUserByUserNumber(String userNumber);

    //根据用户账号修改用户密码
    @Modifying
    @Query("update User u set u.userPwd = ?1 where u.userNumber = ?2")
    int setFixedUserNumber(String userNumber, String userPwd);
}
