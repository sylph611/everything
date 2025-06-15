package org.sylph.everything.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMenuItemRequest {
    private String label;
    private String icon;
    private String path;
    private Integer orderIndex;
    private Boolean isActive;
    private String parentId;
    private Set<String> permissions;
}
