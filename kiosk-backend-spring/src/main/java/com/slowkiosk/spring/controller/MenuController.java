package com.slowkiosk.spring.controller;

import com.slowkiosk.spring.dto.MenuDto;
import com.slowkiosk.spring.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 1. 메뉴 생성 (POST /api/menu)
    @PostMapping
    public ResponseEntity<MenuDto.Response> createMenu(@RequestBody MenuDto.CreateRequest request) {
        MenuDto.Response response = menuService.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 전체 메뉴 조회 (GET /api/menu)
    @GetMapping
    public ResponseEntity<List<MenuDto.Response>> getAllMenus(
            // (선택) 카테고리별 조회를 위한 쿼리 파라미터
            @RequestParam(required = false) String category) {

        List<MenuDto.Response> response;
        if (category != null) {
            response = menuService.getMenusByCategory(category);
        } else {
            response = menuService.getAllMenus();
        }
        return ResponseEntity.ok(response);
    }

    // 3. ID로 메뉴 1개 조회 (GET /api/menu/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MenuDto.Response> getMenuById(@PathVariable Long id) {
        MenuDto.Response response = menuService.getMenuById(id);
        return ResponseEntity.ok(response);
    }

    // 4. 메뉴 수정 (PUT /api/menu/{id})
    @PutMapping("/{id}")
    public ResponseEntity<MenuDto.Response> updateMenu(@PathVariable Long id,
                                                       @RequestBody MenuDto.UpdateRequest request) {
        MenuDto.Response response = menuService.updateMenu(id, request);
        return ResponseEntity.ok(response);
    }

    // 5. 메뉴 삭제 (DELETE /api/menu/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}