package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.ParticipateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipateDAO extends JpaRepository<ParticipateInfo,String>, JpaSpecificationExecutor<ParticipateInfo> {
    @Query("select p from ParticipateInfo p where p.userNumber = ?1")
    List<ParticipateInfo> findAllByUserNumber(String userNumber);

}
