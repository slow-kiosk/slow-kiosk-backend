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

            if (menuRepository.count() == 0) {
                log.info("No menu data found. Initializing simplified data (Burger, Side, Drink only)...");

                // === 1. BURGER (버거 5종) ===
                Menu b001 = new Menu(null, "스테디 와퍼", 6900.0,
                        "참깨빵, 양상추, 양파, 피클, 소고기 패티, 케첩, 마요네즈",
                        "/images/B001.png", "BURGER",
                        "1회 제공량 기준 약 720kcal, 단백질 39.6g, 지방 30.4g, 탄수화물 72.0g, 당류 21.6g, 나트륨 720.0mg / 이 메뉴는 계란, 대두, 밀, 소고기를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "스테디 와퍼,버거,인기");

                Menu b002 = new Menu(null, "불고기 클래식 버거", 5200.0,
                        "참깨빵, 양상추, 양파, 피클, 소고기 패티, 케첩, 마요네즈, 불고기 소스",
                        "/images/B002.png", "BURGER",
                        "1회 제공량 기준 약 560kcal, 단백질 30.8g, 지방 23.6g, 탄수화물 56.0g, 당류 16.8g, 나트륨 560.0mg / 이 메뉴는 계란, 대두, 밀, 소고기를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "불고기 클래식 버거,버거,달콤한맛");

                Menu b003 = new Menu(null, "더블 치즈버거", 5900.0,
                        "참깨빵, 양상추, 양파, 피클, 소고기 패티 2장, 슬라이스 치즈 2장, 케첩, 마요네즈",
                        "/images/B003.png", "BURGER",
                        "1회 제공량 기준 약 610kcal, 단백질 33.5g, 지방 25.8g, 탄수화물 61.0g, 당류 18.3g, 나트륨 610.0mg / 이 메뉴는 계란, 대두, 밀, 소고기, 우유를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "더블 치즈버거,버거,치즈");

                Menu b004 = new Menu(null, "크리스피 치킨버거", 5800.0,
                        "참깨빵, 양상추, 양파, 피클, 치킨 패티, 케첩, 마요네즈",
                        "/images/B004.png", "BURGER",
                        "1회 제공량 기준 약 640kcal, 단백질 35.2g, 지방 27.0g, 탄수화물 64.0g, 당류 19.2g, 나트륨 640.0mg / 이 메뉴는 계란, 닭고기, 대두, 밀를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "크리스피 치킨버거,버거,치킨");

                Menu b005 = new Menu(null, "피쉬 타르타르 버거", 5500.0,
                        "참깨빵, 양상추, 양파, 피클, 생선 패티, 케첩, 마요네즈, 타르타르 소스",
                        "/images/B005.png", "BURGER",
                        "1회 제공량 기준 약 520kcal, 단백질 28.6g, 지방 22.0g, 탄수화물 52.0g, 당류 15.6g, 나트륨 520.0mg / 이 메뉴는 계란, 대두, 밀, 생선를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "피쉬 타르타르 버거,버거,해산물");


                // === 2. SIDE (사이드 5종) ===
                Menu sd201 = new Menu(null, "프렌치프라이 (R)", 2800.0,
                        "감자, 식용유, 소금",
                        "/images/SD201.png", "SIDE",
                        "1회 제공량 기준 약 320kcal, 단백질 8.0g, 지방 16.0g, 탄수화물 36.0g, 당류 10.8g, 나트륨 256.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "프렌치프라이 (R),사이드,감자튀김");

                Menu sd203 = new Menu(null, "치킨너겟 6조각", 3500.0,
                        "닭고기, 튀김옷, 식용유",
                        "/images/SD203.png", "SIDE",
                        "1회 제공량 기준 약 410kcal, 단백질 10.2g, 지방 20.5g, 탄수화물 46.1g, 당류 13.8g, 나트륨 328.0mg / 이 메뉴는 닭고기, 대두, 밀를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "치킨너겟 6조각,사이드,닭고기");

                Menu sd204 = new Menu(null, "어니언 링", 3200.0,
                        "양파, 튀김옷, 식용유",
                        "/images/SD204.png", "SIDE",
                        "1회 제공량 기준 약 360kcal, 단백질 9.0g, 지방 18.0g, 탄수화물 40.5g, 당류 12.2g, 나트륨 288.0mg / 이 메뉴는 대두, 밀를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "어니언 링,사이드,양파");

                Menu sd206 = new Menu(null, "치즈 프라이", 3800.0,
                        "감자, 식용유, 소금, 치즈 소스",
                        "/images/SD206.png", "SIDE",
                        "1회 제공량 기준 약 430kcal, 단백질 10.8g, 지방 21.5g, 탄수화물 48.4g, 당류 14.5g, 나트륨 344.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "치즈 프라이,사이드,치즈");

                Menu sd208 = new Menu(null, "모짜렐라 스틱 4조각", 3700.0,
                        "모짜렐라 치즈, 튀김옷, 식용유",
                        "/images/SD208.png", "SIDE",
                        "1회 제공량 기준 약 390kcal, 단백질 9.8g, 지방 19.5g, 탄수화물 43.9g, 당류 13.2g, 나트륨 312.0mg / 이 메뉴는 대두, 밀, 우유를(을) 함유하고 있어 알레르기가 있는 고객은 섭취에 주의가 필요합니다.",
                        "모짜렐라 스틱 4조각,사이드,치즈");


                // === 3. DRINK (음료 5종) ===
                Menu dr001 = new Menu(null, "콜라 (R)", 2200.0,
                        "탄산수, 콜라 베이스, 설탕, 카페인",
                        "/images/DR001.png", "DRINK",
                        "1회 제공량 기준 약 150kcal, 단백질 0.0g, 지방 0.0g, 탄수화물 37.5g, 당류 33.8g, 나트륨 30.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "콜라,음료,탄산");

                Menu dr003 = new Menu(null, "콜라 제로 (R)", 2200.0,
                        "탄산수, 무설탕 콜라 베이스, 카페인",
                        "/images/DR003.png", "DRINK",
                        "1회 제공량 기준 약 10kcal, 단백질 0.0g, 지방 0.0g, 탄수화물 0.0g, 당류 0.0g, 나트륨 2.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "콜라 제로,음료,탄산,제로칼로리");

                Menu dr005 = new Menu(null, "사이다 (R)", 2200.0,
                        "탄산수, 레몬라임 향료, 설탕",
                        "/images/DR005.png", "DRINK",
                        "1회 제공량 기준 약 150kcal, 단백질 0.0g, 지방 0.0g, 탄수화물 37.5g, 당류 33.8g, 나트륨 30.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "사이다,음료,탄산");

                Menu dr007 = new Menu(null, "오렌지 주스 (R)", 2200.0,
                        "과일 농축액, 물, 설탕",
                        "/images/DR007.png", "DRINK",
                        "1회 제공량 기준 약 150kcal, 단백질 0.0g, 지방 0.0g, 탄수화물 37.5g, 당류 33.8g, 나트륨 30.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "오렌지 주스,음료,과일");

                Menu dr011 = new Menu(null, "아이스 아메리카노 (R)", 2200.0,
                        "에스프레소, 물, 얼음",
                        "/images/DR011.png", "DRINK",
                        "1회 제공량 기준 약 10kcal, 단백질 0.0g, 지방 0.0g, 탄수화물 2.5g, 당류 2.2g, 나트륨 2.0mg / 이 메뉴에 대한 상세 알레르기 정보는 제한적으로 제공됩니다.",
                        "아이스 아메리카노,음료,커피");


                // DB 저장 (총 15개 메뉴)
                menuRepository.saveAll(List.of(
                        // Burger
                        b001, b002, b003, b004, b005,
                        // Side
                        sd201, sd203, sd204, sd206, sd208,
                        // Drink
                        dr001, dr003, dr005, dr007, dr011
                ));

                log.info("Simplified menu data initialization complete. {} menus added.", menuRepository.count());
            } else {
                log.info("Menu data already exists. ({} items)", menuRepository.count());
            }
        };
    }
}