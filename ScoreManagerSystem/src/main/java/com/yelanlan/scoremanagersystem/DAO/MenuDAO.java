package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuDAO extends JpaRepository<Menu,String> {
    @Query("select  m from Menu m where m.menuId = ?1 order by m.order")
    Menu findMenuByMenuId(String menuId);


}
