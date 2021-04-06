package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDAO extends JpaRepository<Menu,String> {
    @Query("select  m from Menu m where m.menuId = ?1 order by m.orders")
    Menu findMenuByMenuId(String menuId);

    @Query("select m from Menu m where m.type = ?1 order by  m.orders")
    List<Menu> findAllByType(String type);

    @Query("select m from Menu m where m.type = ?1 and m.menuId = ?2 order by m.orders")
    Menu findByTypeAndMenuId(String type,String menuId);

}
