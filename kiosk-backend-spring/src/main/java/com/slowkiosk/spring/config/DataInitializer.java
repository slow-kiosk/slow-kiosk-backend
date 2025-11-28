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
                log.info("No menu data found. Initializing data with Scenario Tags...");

                // ==========================================
                // 1. BURGER (버거)
                // ==========================================
                Menu b001 = new Menu(null, "스테디 와퍼", 6900.0,
                        "참깨빵, 양상추, 양파, 피클, 소고기 패티, 케첩, 마요네즈",
                        "/images/B001.png", "BURGER",
                        "1회 제공량 기준 약 720kcal, 단백질 39.6g, 지방 30.4g, 탄수화물 72.0g / 알레르기: 밀,대두,소고기",
                        "스테디 와퍼,버거,인기,베스트,대표메뉴,호불호없는,든든한"); // A-1

                Menu b002 = new Menu(null, "불고기 클래식 버거", 5200.0,
                        "참깨빵, 양상추, 양파, 피클, 소고기 패티, 불고기 소스, 마요네즈",
                        "/images/B002.png", "BURGER",
                        "1회 제공량 기준 약 560kcal, 단백질 30.8g, 지방 23.6g / 알레르기: 밀,대두,소고기",
                        "불고기 클래식 버거,버거,달콤한맛,부드러운,어르신추천,아이추천,추억의맛,맵지않은"); // B-8, C-12, G-31, H-39

                Menu b003 = new Menu(null, "더블 치즈버거", 5900.0,
                        "참깨빵, 소고기 패티 2장, 슬라이스 치즈 2장, 케첩, 마요네즈",
                        "/images/B003.png", "BURGER",
                        "1회 제공량 기준 약 610kcal, 단백질 33.5g, 지방 25.8g / 알레르기: 밀,대두,소고기,우유",
                        "더블 치즈버거,버거,치즈,느끼한,해장,기름진,고기매니아,야채없는,대용량"); // A-2, A-5, G-34, G-35

                Menu b004 = new Menu(null, "크리스피 치킨버거", 5800.0,
                        "참깨빵, 양상추, 피클, 치킨 패티, 마요네즈",
                        "/images/B004.png", "BURGER",
                        "1회 제공량 기준 약 640kcal, 단백질 35.2g / 알레르기: 밀,대두,닭고기",
                        "크리스피 치킨버거,버거,치킨,바삭한,가성비,학생추천,담백한"); // B-10, H-40

                Menu b005 = new Menu(null, "피쉬 타르타르 버거", 5500.0,
                        "참깨빵, 양상추, 생선 패티, 타르타르 소스",
                        "/images/B005.png", "BURGER",
                        "1회 제공량 기준 약 520kcal / 알레르기: 밀,대두,생선",
                        "피쉬 타르타르 버거,버거,해산물,부드러운,담백한,순한맛,비육류"); // G-33

                // [추가] 매운 쉬림프 버거 (스트레스 해소용)
                Menu bl010 = new Menu(null, "매운 쉬림프 버거", 9200.0,
                        "참깨빵, 양상추, 새우 패티, 스파이시 소스",
                        "/images/BL010.png", "BURGER",
                        "1회 제공량 기준 약 850kcal / 알레르기: 밀,대두,새우",
                        "한정,매운,매운 쉬림프 버거,스트레스,화끈한,자극적인,빨간거"); // A-4, E-22

                // [신규] 베지테리언 버거 (속 편한, 다이어트)
                Menu b011 = new Menu(null, "베지테리언 버거", 6200.0,
                        "참깨빵, 양상추, 토마토, 양파, 식물성 패티, 소이 마요네즈",
                        "/images/B011.png", "BURGER",
                        "1회 제공량 기준 약 420kcal / 알레르기: 밀,대두",
                        "베지테리언 버거,버거,비건,채식,속편한,가벼운,다이어트,소화잘되는"); // A-6, C-13


                // ==========================================
                // 2. SET (세트)
                // ==========================================
                // [신규] 어린이 버거 세트 (장난감 포함)
                Menu s201 = new Menu(null, "어린이 버거 세트", 5500.0,
                        "키즈 불고기 버거, 스몰 프렌치프라이, 우유, 장난감",
                        "/images/S201.png", "SET",
                        "1회 제공량 기준 약 550kcal / 알레르기: 밀,대두,우유,소고기",
                        "어린이 버거 세트,세트,키즈,장난감,아이추천,순한맛,안매운,선물"); // H-38, E-23


                // ==========================================
                // 3. SIDE (사이드)
                // ==========================================
                Menu sd201 = new Menu(null, "프렌치프라이 (R)", 2800.0,
                        "감자, 식용유, 소금",
                        "/images/SD201.png", "SIDE",
                        "1회 제공량 기준 약 320kcal / 알레르기: 없음",
                        "프렌치프라이 (R),사이드,감자튀김,짭짤한,간식,기본");

                Menu sd203 = new Menu(null, "치킨너겟 6조각", 3500.0,
                        "닭고기, 튀김옷",
                        "/images/SD203.png", "SIDE",
                        "1회 제공량 기준 약 410kcal / 알레르기: 밀,대두,닭고기",
                        "치킨너겟 6조각,사이드,닭고기,순한맛,아이추천,간식,부드러운");

                Menu sd204 = new Menu(null, "어니언 링", 3200.0,
                        "양파, 튀김옷",
                        "/images/SD204.png", "SIDE",
                        "1회 제공량 기준 약 360kcal / 알레르기: 밀,대두",
                        "어니언 링,사이드,양파,바삭한");

                // [추가] 코울슬로 (소화, 상큼함)
                Menu sd205 = new Menu(null, "코울슬로", 2500.0,
                        "양배추, 당근, 마요네즈 드레싱",
                        "/images/SD205.png", "SIDE",
                        "1회 제공량 기준 약 150kcal / 알레르기: 대두,계란",
                        "코울슬로,사이드,샐러드,상큼한,소화잘되는,속편한,가벼운"); // C-13, G-36

                Menu sd206 = new Menu(null, "치즈 프라이", 3800.0,
                        "감자, 치즈 소스",
                        "/images/SD206.png", "SIDE",
                        "1회 제공량 기준 약 430kcal / 알레르기: 우유",
                        "치즈 프라이,사이드,치즈,느끼한,고소한");

                Menu sd208 = new Menu(null, "모짜렐라 스틱 4조각", 3700.0,
                        "모짜렐라 치즈, 튀김옷",
                        "/images/SD208.png", "SIDE",
                        "1회 제공량 기준 약 390kcal / 알레르기: 밀,대두,우유",
                        "모짜렐라 스틱 4조각,사이드,치즈,아이추천,늘어나는");

                // [신규] 치킨랩 (소식, 간단)
                Menu sd211 = new Menu(null, "치킨랩", 3800.0,
                        "또띠아, 치킨 텐더, 양상추, 머스타드 소스",
                        "/images/SD211.png", "SIDE",
                        "1회 제공량 기준 약 350kcal / 알레르기: 밀,대두,닭고기",
                        "치킨랩,사이드,치킨,스낵,가벼운,소식,간단한,입맛없을때"); // A-3, B-11

                // [신규] 리코타 치즈 샐러드 (다이어트, 밀가루X)
                Menu sd212 = new Menu(null, "리코타 치즈 샐러드", 4800.0,
                        "양상추, 리코타 치즈, 토마토, 발사믹",
                        "/images/SD212.png", "SIDE",
                        "1회 제공량 기준 약 210kcal / 알레르기: 우유,토마토",
                        "리코타 치즈 샐러드,사이드,샐러드,건강,다이어트,가벼운,밀가루없는,살안찌는"); // A-6, C-13

                // [신규] 머쉬룸 스프 (치아 불편, 부드러움)
                Menu sd213 = new Menu(null, "머쉬룸 스프", 3500.0,
                        "양송이, 크림, 우유, 크루통",
                        "/images/SD213.png", "SIDE",
                        "1회 제공량 기준 약 180kcal / 알레르기: 우유,밀",
                        "머쉬룸 스프,사이드,스프,따뜻한,부드러운,어르신추천,속편한,아침,치아불편"); // C-12

                // [신규] 토마토 파스타
                Menu sd214 = new Menu(null, "토마토 파스타", 4200.0,
                        "파스타면, 토마토 소스, 치즈",
                        "/images/SD214.png", "SIDE",
                        "1회 제공량 기준 약 380kcal / 알레르기: 밀,우유,토마토",
                        "토마토 파스타,사이드,면요리,식사대용,새콤달콤,아이추천");


                // ==========================================
                // 4. DRINK (음료)
                // ==========================================
                Menu dr001 = new Menu(null, "콜라 (R)", 2200.0,
                        "탄산수, 시럽",
                        "/images/DR001.png", "DRINK",
                        "150kcal / 카페인",
                        "콜라,음료,탄산,시원한,기본,느끼함해소");

                Menu dr003 = new Menu(null, "콜라 제로 (R)", 2200.0,
                        "탄산수, 감미료",
                        "/images/DR003.png", "DRINK",
                        "0kcal / 카페인",
                        "콜라 제로,음료,탄산,제로칼로리,다이어트,살안찌는"); // A-6

                Menu dr005 = new Menu(null, "사이다 (R)", 2200.0,
                        "탄산수, 레몬향",
                        "/images/DR005.png", "DRINK",
                        "150kcal",
                        "사이다,음료,탄산,시원한,깔끔한");

                Menu dr007 = new Menu(null, "오렌지 주스 (R)", 2200.0,
                        "오렌지 농축액",
                        "/images/DR007.png", "DRINK",
                        "150kcal",
                        "오렌지 주스,음료,과일,상큼한,아이추천,비타민");

                Menu dr011 = new Menu(null, "아이스 아메리카노 (R)", 2200.0,
                        "에스프레소, 물, 얼음",
                        "/images/DR011.png", "DRINK",
                        "10kcal / 고카페인",
                        "아이스 아메리카노,음료,커피,차가운,깔끔한,어른음료,졸음방지");

                // [추가] 핫 아메리카노
                Menu dr013 = new Menu(null, "핫 아메리카노 (R)", 2200.0,
                        "에스프레소, 뜨거운 물",
                        "/images/DR013.png", "DRINK",
                        "10kcal / 고카페인",
                        "핫 아메리카노,음료,따뜻한,커피,깔끔한,몸녹이는");

                // [신규] 마시멜로 핫초코 (스트레스, 당충전)
                Menu dr021 = new Menu(null, "마시멜로 핫초코", 3500.0,
                        "우유, 초코 파우더, 마시멜로",
                        "/images/DR021.png", "DRINK",
                        "280kcal / 우유",
                        "마시멜로 핫초코,음료,따뜻한,달콤한맛,당충전,아이추천,스트레스,기분전환"); // A-4


                // ==========================================
                // 5. DESSERT (디저트)
                // ==========================================
                Menu ds001 = new Menu(null, "소프트 아이스크림", 2000.0,
                        "우유, 설탕",
                        "/images/DS001.png", "DESSERT",
                        "190kcal / 우유",
                        "소프트 아이스크림,디저트,시원한,달콤한,부드러운,입가심,아이추천");

                Menu ds002 = new Menu(null, "초코 선데", 2700.0,
                        "우유, 초코 시럽",
                        "/images/DS002.png", "DESSERT",
                        "260kcal / 우유",
                        "초코 선데,디저트,달콤한,시원한,당충전");

                Menu ds003 = new Menu(null, "애플 파이", 2300.0,
                        "밀가루, 사과잼, 계피",
                        "/images/DS003.png", "DESSERT",
                        "230kcal / 밀",
                        "애플 파이,디저트,따뜻한,달콤한,간식,바삭한");

                Menu ds009 = new Menu(null, "밀크셰이크 바닐라", 3500.0,
                        "우유, 바닐라 아이스크림",
                        "/images/DS009.png", "DESSERT",
                        "390kcal / 우유",
                        "밀크셰이크 바닐라,디저트,마시는,달콤한,부드러운,시원한");


                // DB 저장
                menuRepository.saveAll(List.of(
                        // Burger
                        b001, b002, b003, b004, b005, bl010, b011,
                        // Set
                        s201,
                        // Side
                        sd201, sd203, sd204, sd205, sd206, sd208, sd211, sd212, sd213, sd214,
                        // Drink
                        dr001, dr003, dr005, dr007, dr011, dr013, dr021,
                        // Dessert
                        ds001, ds002, ds003, ds009
                ));

                log.info("Menu data initialization complete with TAGS. {} menus added.", menuRepository.count());
            } else {
                log.info("Menu data already exists. ({} items)", menuRepository.count());
            }
        };
    }
}