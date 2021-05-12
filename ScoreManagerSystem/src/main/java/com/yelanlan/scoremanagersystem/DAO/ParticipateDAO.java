package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.UserScoreDTO;
import com.yelanlan.scoremanagersystem.RepositoryImpl.ParticipateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ParticipateDAO extends JpaRepository<ParticipateInfo,String>, JpaSpecificationExecutor<ParticipateInfo> {
    @Query("select p from ParticipateInfo p where p.userNumber = ?1")
    List<ParticipateInfo> findAllByUserNumber(String userNumber);

    @Query("select p from ParticipateInfo p where p.id = ?1")
    ParticipateInfo findAllById(String id);

    @Transactional
    @Modifying
    @Query("update ParticipateInfo p set p.certImg = ?1 where p.id = ?2")
    int updatePartInfo(String certImg,String id);

    @Query("select p from ParticipateInfo  p where p.userNumber=?1 and p.actId=?2")
    ParticipateInfo findAllByUserNumberAndActId(String userNumber,String actId);

    @Query("select new com.yelanlan.scoremanagersystem.RepositoryImpl.DTO.UserScoreDTO(u.userNumber,u.userName,u.headAvatar,d.departName,p.measureScore) " +
            "from ParticipateInfo p,User u,Department d where p.actId=?1 and u.userNumber = p.userNumber and u.departmentId = d.id order by p.measureScore desc")
    List<UserScoreDTO> getActScoreSort(String actId);


}
