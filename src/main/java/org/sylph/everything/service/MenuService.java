package org.sylph.everything.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sylph.everything.dto.CreateMenuItemRequest;
import org.sylph.everything.dto.MenuItemDto;
import org.sylph.everything.dto.UpdateMenuItemRequest;
import org.sylph.everything.entity.MenuItem;
import org.sylph.everything.repository.MenuItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    // 전체 메뉴 트리 조회
    public List<MenuItemDto> getMenuTree() {
        List<MenuItem> roots = menuItemRepository.findAllByParentIsNullOrderByOrderIndexAsc();
        return roots.stream()
                .map(this::toDtoWithChildren)
                .collect(Collectors.toList());
    }

    // 메뉴 생성
    public MenuItemDto createMenu(CreateMenuItemRequest request) {
        MenuItem menu = MenuItem.builder()
                .label(request.getLabel())
                .icon(request.getIcon())
                .path(request.getPath())
                .orderIndex(request.getOrderIndex())
                .isActive(true)
                .permissions(Optional.ofNullable(request.getPermissions()).orElse(Set.of()))
                .build();

        if (request.getParentId() != null) {
            MenuItem parent = menuItemRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parentId"));
            menu.setParent(parent);
        }

        return toDto(menuItemRepository.save(menu));
    }

    // 메뉴 수정
    public MenuItemDto updateMenu(String id, UpdateMenuItemRequest request) {
        MenuItem menu = menuItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Menu not found"));

        Optional.ofNullable(request.getLabel()).ifPresent(menu::setLabel);
        Optional.ofNullable(request.getIcon()).ifPresent(menu::setIcon);
        Optional.ofNullable(request.getPath()).ifPresent(menu::setPath);
        Optional.ofNullable(request.getOrderIndex()).ifPresent(menu::setOrderIndex);
        Optional.ofNullable(request.getIsActive()).ifPresent(menu::setActive);
        Optional.ofNullable(request.getPermissions()).ifPresent(menu::setPermissions);

        if (request.getParentId() != null) {
            MenuItem parent = menuItemRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parentId"));
            menu.setParent(parent);
        } else {
            menu.setParent(null);
        }

        return toDto(menuItemRepository.save(menu));
    }

    // 메뉴 삭제
    public void deleteMenu(String id) {
        if (!menuItemRepository.existsById(id)) {
            throw new NoSuchElementException("Menu not found");
        }
        menuItemRepository.deleteById(id);
    }

    // === private helpers ===
    private MenuItemDto toDto(MenuItem menu) {
        return MenuItemDto.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .icon(menu.getIcon())
                .path(menu.getPath())
                .orderIndex(menu.getOrderIndex())
                .isActive(menu.isActive())
                .parentId(menu.getParent() != null ? menu.getParent().getId() : null)
                .permissions(menu.getPermissions())
                .build();
    }

    private MenuItemDto toDtoWithChildren(MenuItem menu) {
        List<MenuItemDto> children = menu.getChildren().stream()
                .sorted(Comparator.comparingInt(MenuItem::getOrderIndex))
                .map(this::toDtoWithChildren)
                .collect(Collectors.toList());

        MenuItemDto dto = toDto(menu);
        dto.setChildren(children);
        return dto;
    }
}