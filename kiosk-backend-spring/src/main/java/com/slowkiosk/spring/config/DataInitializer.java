package com.slowkiosk.spring.config;

import com.slowkiosk.spring.entity.Menu;
import com.slowkiosk.spring.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Profile("!prod") // "prod" (운영) 프로필이 아닐 때만 실행
public class DataInitializer {

    private final MenuRepository menuRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            log.info("Checking for existing menu data...");

            // 1. DB에 메뉴 데이터가 아무것도 없을 때만 초기 데이터 삽입
            if (menuRepository.count() == 0) {
                log.info("No menu data found. Initializing test data...");

                // 2. 테스트용 메뉴 객체 생성
                Menu burger = new Menu();
                burger.setName("치즈버거");
                burger.setPrice(5500.0);
                burger.setCategory("BURGER");
                burger.setDescription("클래식한 100% 비프 치즈버거");
                burger.setImageUrl("/images/cheese-burger.png");

                Menu coke = new Menu();
                coke.setName("콜라");
                coke.setPrice(2000.0);
                coke.setCategory("DRINK");
                coke.setDescription("시원한 코카콜라");
                coke.setImageUrl("/images/coke.png");

                Menu fries = new Menu();
                fries.setName("프렌치 프라이");
                fries.setPrice(2500.0);
                fries.setCategory("SIDE");
                fries.setDescription("바삭한 감자튀김");
                fries.setImageUrl("/images/fries.png");

                // 3. 리스트로 묶어 DB에 한 번에 저장
                menuRepository.saveAll(List.of(burger, coke, fries));

                log.info("Test data initialization complete. {} menus added.", menuRepository.count());
            } else {
                log.info("Menu data already exists. ({} items)", menuRepository.count());
            }
        };
    }
}