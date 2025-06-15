package org.sylph.everything.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String label;

    private String icon;

    private String path;

    private int orderIndex;

    private boolean isActive;

    // 계층형 메뉴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MenuItem parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> children = new ArrayList<>();

    // 권한 목록 (별도 테이블 매핑)
    @ElementCollection
    @CollectionTable(name = "menu_permissions", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "permission")
    private Set<String> permissions = new HashSet<>();
}