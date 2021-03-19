package com.yelanlan.scoremanagersystem.DAO;


import com.yelanlan.scoremanagersystem.RepositoryImpl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDAO  extends JpaRepository<User,String> {
    @Query("select u from User u where u.userNumber = :userNumber")
    User findUserByUserNumber(String userNumber);

}
