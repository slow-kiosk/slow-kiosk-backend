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
                log.info("No menu data found. Initializing test data with nutrition info...");

                // (참고) Menu 생성자 순서: id, name, price, description, imageUrl, category, nutrition, tags

                // === 버거류 ===
                Menu burger1 = new Menu(
                        null,
                        "치즈버거",
                        5500.0,
                        "진한 치즈 풍미가 일품인 클래식 버거",
                        "/images/cheese-burger.png",
                        "BURGER",
                        "열량: 450kcal / 알레르기: 밀, 우유, 대두",
                        "베스트,클래식"
                );

                Menu burger2 = new Menu(
                        null,
                        "불고기 버거",
                        6000.0,
                        "한국식 불고기 소스가 듬뿍 들어간 버거",
                        "/images/bulgogi-burger.png",
                        "BURGER",
                        "열량: 480kcal / 알레르기: 밀, 대두",
                        "인기,달콤한맛"
                );

                Menu burger3 = new Menu(
                        null,
                        "새우 버거",
                        6200.0,
                        "통통한 새우살이 씹히는 씨푸드 버거",
                        "/images/shrimp-burger.png",
                        "BURGER",
                        "열량: 420kcal / 알레르기: 새우, 밀",
                        "신메뉴,해산물"
                );

                // === 음료류 ===
                Menu drink1 = new Menu(
                        null,
                        "콜라",
                        2000.0,
                        "톡 쏘는 시원한 코카콜라",
                        "/images/coke.png",
                        "DRINK",
                        "열량: 110kcal / 알레르기: 없음",
                        "탄산,기본"
                );

                Menu drink2 = new Menu(
                        null,
                        "사이다",
                        2000.0,
                        "청량감이 가득한 칠성사이다",
                        "/images/sprite.png",
                        "DRINK",
                        "열량: 100kcal / 알레르기: 없음",
                        "탄산,깔끔함"
                );

                Menu drink3 = new Menu(
                        null,
                        "환타",
                        2000.0,
                        "상큼한 오렌지향 환타",
                        "/images/fanta.png",
                        "DRINK",
                        "열량: 120kcal / 알레르기: 없음",
                        "탄산,과일향"
                );

                // === 사이드류 ===
                Menu side1 = new Menu(
                        null,
                        "프렌치 프라이",
                        2500.0,
                        "겉은 바삭하고 속은 촉촉한 감자튀김",
                        "/images/fries.png",
                        "SIDE",
                        "열량: 320kcal / 알레르기: 대두",
                        "튀김,기본"
                );

                Menu side2 = new Menu(
                        null,
                        "치즈 스틱",
                        2800.0,
                        "고소한 모짜렐라 치즈가 늘어나는 스틱",
                        "/images/cheese-sticks.png",
                        "SIDE",
                        "열량: 250kcal / 알레르기: 우유, 밀",
                        "튀김,치즈"
                );

                Menu side3 = new Menu(
                        null,
                        "어니언 링",
                        2700.0,
                        "달콤한 양파를 바삭하게 튀겨낸 링",
                        "/images/onion-rings.png",
                        "SIDE",
                        "열량: 300kcal / 알레르기: 밀",
                        "튀김,양파"
                );

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