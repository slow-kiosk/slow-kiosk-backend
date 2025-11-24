package com.slowkiosk.spring.entity;

import com.slowkiosk.spring.dto.MenuDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // DataInitializer에서 모든 필드를 포함한 생성자를 쓰기 위해 필요
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private double price;

    private String description; // 메뉴 설명

    private String imageUrl;

    private String category; // BURGER, SIDE, DRINK 등

    // [신규 추가] 영양 성분 (예: "500kcal, 단백질 20g")
    // AI가 "칼로리가 어떻게 돼?" 같은 질문에 답하기 위해 필요
    private String nutrition;

    // [신규 추가] 태그 (예: "베스트,매운맛")
    // AI가 "매운거 추천해줘" 같은 질문에 답하기 위해 필요
    private String tags;

    // DTO를 기반으로 엔티티를 생성하는 생성자
    public Menu(MenuDto.CreateRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.imageUrl = request.getImageUrl();
        this.category = request.getCategory();
        this.nutrition = request.getNutrition(); // 추가됨
        this.tags = request.getTags();           // 추가됨
    }

    // 정보 업데이트 메서드
    public void update(MenuDto.UpdateRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.imageUrl = request.getImageUrl();
        this.category = request.getCategory();
        this.nutrition = request.getNutrition(); // 추가됨
        this.tags = request.getTags();           // 추가됨
    }
}