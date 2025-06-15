package org.sylph.everything.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto {

    private String id;
    private String label;
    private String icon;
    private String path;
    private Integer orderIndex;
    private boolean isActive;
    private String parentId;
    private Set<String> permissions;

    private List<MenuItemDto> children;
}
