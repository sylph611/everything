package org.sylph.everything.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sylph.everything.entity.MenuItem;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, String> {

    List<MenuItem> findAllByParentIsNullOrderByOrderIndexAsc();

    List<MenuItem> findByParentIdOrderByOrderIndexAsc(String parentId);
}