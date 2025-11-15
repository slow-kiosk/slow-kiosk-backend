package com.slowkiosk.spring.repository;

import com.slowkiosk.spring.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // (선택) 카테고리별 메뉴 조회를 위한 쿼리 메서드
    List<Menu> findByCategory(String category);


}