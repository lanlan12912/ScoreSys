package com.yelanlan.scoremanagersystem.DAO;

import com.yelanlan.scoremanagersystem.RepositoryImpl.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
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

    @Query("select m from Menu m where m.menuId in (?1) or m.parentId is null order by m.orders")
    List<Menu> findAllByMenuIdIn(List<String> menuIds);

    @Transactional
    @Modifying
    @Query("delete from Menu m where  m.parentId = ?1")
    void deleteAllByParentId(String parentId);

    @Query("select m from Menu m where m.parentId = ?1")
    List<Menu> findAllByParentId(String parentId);

    @Transactional
    @Modifying
    @Query("delete from Menu m where m.menuId in (?1)")
    void deleteAllByMenuIds(List<String> menuIds);
}
