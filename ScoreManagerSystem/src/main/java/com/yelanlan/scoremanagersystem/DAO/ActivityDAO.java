package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface ActivityDAO extends JpaRepository<Activity,String>, JpaSpecificationExecutor<Activity> {
    @Query("select a from Activity a where a.id in (?1)")
    List<Activity> findAllByIds(List<String> ids);

    @Query("select a from Activity a where a.id =?1")
    Activity findAllById(String id);

    @Transactional
    @Modifying
    @Query("update Activity a set a.delFlag = 1 where a.id = ?1")
    int delActById(String id);

    @Transactional
    @Modifying
    @Query("update Activity a set a.actImgs = ?1 where a.id = ?2")
    int updateActImgs(String imgs,String id);

    @Query("select a from Activity a where a.startDate >=?1 and a.endDate<=?2")
    List<Activity> findAllByDate(Date startDate,Date endDate);


}
