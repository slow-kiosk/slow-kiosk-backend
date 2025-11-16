package com.slowkiosk.spring.repository;

import com.slowkiosk.spring.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional; // <-- 추가

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByCategory(String category);

    Optional<Menu> findByName(String name);
}