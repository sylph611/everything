package org.sylph.everything.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.sylph.everything.entity.MenuItem;
import org.sylph.everything.entity.User;
import org.sylph.everything.repository.MenuItemRepository;
import org.sylph.everything.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("기본 데이터 초기화 시작...");
        
        // 어드민 유저 초기화
        initializeAdminUser();
        
        // 메뉴 데이터 초기화
        initializeMenuData();
        
        log.info("기본 데이터 초기화 완료!");
    }

    private void initializeAdminUser() {
        // 기존 어드민 유저가 있으면 초기화하지 않음
        if (userRepository.existsByEmail("admin@naver.com")) {
            log.info("어드민 유저가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        User adminUser = User.builder()
                .email("admin@naver.com")
                .name("관리자")
                .password(passwordEncoder.encode("admin123")) // 기본 비밀번호: admin123
                .provider("local")
                .providerId("admin")
                .role("ADMIN")
                .build();

        userRepository.save(adminUser);
        log.info("어드민 유저가 생성되었습니다. 이메일: admin@naver.com, 비밀번호: admin123");
    }

    private void initializeMenuData() {
        // 기존 메뉴 데이터가 있으면 초기화하지 않음
        if (menuItemRepository.count() > 0) {
            log.info("메뉴 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        // 메인 메뉴들 생성
        MenuItem dashboard = createMenuItem("대시보드", "-", "/dashboard", 1, true, null);
        MenuItem userManagement = createMenuItem("사용자 관리", "-", "/users", 2, true, null);
        MenuItem settings = createMenuItem("설정", "-", "/settings", 3, true, null);
        MenuItem reports = createMenuItem("보고서", "-", "/reports", 4, true, null);

        // 사용자 관리 하위 메뉴들
        MenuItem userList = createMenuItem("사용자 목록", "-", "/users/list", 1, true, userManagement);
        MenuItem userCreate = createMenuItem("사용자 생성", "-", "/users/create", 2, true, userManagement);
        MenuItem userRoles = createMenuItem("역할 관리", "-", "/users/roles", 3, true, userManagement);

        // 설정 하위 메뉴들
        MenuItem generalSettings = createMenuItem("일반 설정", "-", "/settings/general", 1, true, settings);
        MenuItem securitySettings = createMenuItem("보안 설정", "-", "/settings/security", 2, true, settings);
        MenuItem notificationSettings = createMenuItem("알림 설정", "-", "/settings/notifications", 3, true, settings);

        // 보고서 하위 메뉴들
        MenuItem salesReport = createMenuItem("매출 보고서", "-", "/reports/sales", 1, true, reports);
        MenuItem userReport = createMenuItem("사용자 보고서", "-", "/reports/users", 2, true, reports);
        MenuItem systemReport = createMenuItem("시스템 보고서", "-", "/reports/system", 3, true, reports);

        // 메인 메뉴들 저장
        menuItemRepository.saveAll(Arrays.asList(dashboard, userManagement, settings, reports));

        // 하위 메뉴들 저장
        menuItemRepository.saveAll(Arrays.asList(
            userList, userCreate, userRoles,
            generalSettings, securitySettings, notificationSettings,
            salesReport, userReport, systemReport
        ));

        log.info("기본 메뉴 데이터 초기화 완료! 총 {}개의 메뉴가 생성되었습니다.", menuItemRepository.count());
    }

    private MenuItem createMenuItem(String label, String icon, String path, int orderIndex, boolean isActive, MenuItem parent) {
        Set<String> permissions = new HashSet<>();
        
        // 기본 권한 설정 (실제 프로젝트에 맞게 조정 필요)
        if (label.contains("관리") || label.contains("설정")) {
            permissions.add("ADMIN");
        } else {
            permissions.add("USER");
        }

        return MenuItem.builder()
                .label(label)
                .icon(icon)
                .path(path)
                .orderIndex(orderIndex)
                .isActive(isActive)
                .parent(parent)
                .permissions(permissions)
                .build();
    }
} 