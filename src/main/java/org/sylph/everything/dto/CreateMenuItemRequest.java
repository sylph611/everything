package org.sylph.everything.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMenuItemRequest {
    private String label;
    private String icon;
    private String path;
    private Integer orderIndex;
    private String parentId;
    private Set<String> permissions;
}