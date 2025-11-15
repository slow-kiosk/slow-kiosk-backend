package com.slowkiosk.spring.service;

import com.slowkiosk.spring.dto.MenuDto;
import com.slowkiosk.spring.entity.Menu;
import com.slowkiosk.spring.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 사용
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * 새 메뉴 생성
     */
    @Transactional // 쓰기 작업이므로 readOnly=false 적용
    public MenuDto.Response createMenu(MenuDto.CreateRequest request) {
        Menu menu = new Menu(request);
        Menu savedMenu = menuRepository.save(menu);
        return MenuDto.Response.fromEntity(savedMenu);
    }

    /**
     * ID로 메뉴 1개 조회
     */
    public MenuDto.Response getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + id));
        return MenuDto.Response.fromEntity(menu);
    }

    /**
     * 모든 메뉴 조회
     */
    public List<MenuDto.Response> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(MenuDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * (선택) 카테고리로 메뉴 조회
     */
    public List<MenuDto.Response> getMenusByCategory(String category) {
        return menuRepository.findByCategory(category).stream()
                .map(MenuDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 정보 수정
     */
    @Transactional
    public MenuDto.Response updateMenu(Long id, MenuDto.UpdateRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + id));

        menu.update(request); // 엔티티 내부 메서드로 업데이트
        // JPA의 'Dirty Checking'으로 인해 save()를 호출하지 않아도 트랜잭션 종료 시 DB에 반영됩니다.

        return MenuDto.Response.fromEntity(menu);
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found with id: " + id);
        }
        menuRepository.deleteById(id);
    }
}