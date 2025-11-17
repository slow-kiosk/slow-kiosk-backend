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

                // (참고) Menu 엔티티의 @AllArgsConstructor를 사용합니다.
                // (id, name, price, description, imageUrl, category)

                // === 버거류 ===
                Menu burger1 = new Menu(null, "치즈버거", 5500.0, "클래식한 100% 비프 치즈버거", "/images/cheese-burger.png", "BURGER");
                Menu burger2 = new Menu(null, "불고기 버거", 6000.0, "한국식 불고기 소스 버거", "/images/bulgogi-burger.png", "BURGER");
                Menu burger3 = new Menu(null, "새우 버거", 6200.0, "통통한 새우 패티 버거", "/images/shrimp-burger.png", "BURGER");

                // === 음료류 ===
                Menu drink1 = new Menu(null, "콜라", 2000.0, "시원한 코카콜라", "/images/coke.png", "DRINK");
                Menu drink2 = new Menu(null, "사이다", 2000.0, "시원한 칠성사이다", "/images/sprite.png", "DRINK");
                Menu drink3 = new Menu(null, "환타", 2000.0, "상큼한 환타 오렌지", "/images/fanta.png", "DRINK");

                // === 사이드류 ===
                Menu side1 = new Menu(null, "프렌치 프라이", 2500.0, "바삭한 감자튀김", "/images/fries.png", "SIDE");
                Menu side2 = new Menu(null, "치즈 스틱", 2800.0, "고소한 롯데리아 치즈 스틱", "/images/cheese-sticks.png", "SIDE");
                Menu side3 = new Menu(null, "어니언 링", 2700.0, "달콤한 양파 링", "/images/onion-rings.png", "SIDE");


                // 3. 리스트로 묶어 DB에 한 번에 저장
                menuRepository.saveAll(List.of(
                        burger1, burger2, burger3,
                        drink1, drink2, drink3,
                        side1, side2, side3
                ));

                log.info("Test data initialization complete. {} menus added.", menuRepository.count());
            } else {
                log.info("Menu data already exists. ({} items)", menuRepository.count());
            }
        };
    }
}