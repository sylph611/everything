package org.sylph.everything.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sylph.everything.dto.CreateMenuItemRequest;
import org.sylph.everything.dto.MenuItemDto;
import org.sylph.everything.dto.UpdateMenuItemRequest;
import org.sylph.everything.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 전체 메뉴 트리 조회
    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getAllMenus() {
        return ResponseEntity.ok(menuService.getMenuTree());
    }

    // 메뉴 항목 생성
    @PostMapping
    public ResponseEntity<MenuItemDto> createMenu(@RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.ok(menuService.createMenu(request));
    }

    // 메뉴 항목 수정
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDto> updateMenu(@PathVariable String id,
                                                  @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(menuService.updateMenu(id, request));
    }

    // 메뉴 항목 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}
