package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDAO extends JpaRepository<Menu,String> {
    @Query("select  m from Menu m where m.menuId = ?1 order by m.orders asc ")
    Menu findMenuByMenuId(String menuId);

    @Query("select m from Menu m where m.type = ?1 order by  m.orders asc ")
    List<Menu> findAllByType(String type);

    @Query("select m from Menu m where m.type = ?1 and m.menuId = ?2 order by m.orders asc ")
    Menu findByTypeAndMenuId(String type,String menuId);

    @Query("select m  from Menu m order by m.orders asc")
    List<Menu> findAllOrOrderByOrders();

    @Query("select m from Menu m where m.menuId in (?1) order by m.orders")
    List<Menu> findAllByMenuIdIn(List<String> menuIds);
}
