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
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private double price;

    private String description;

    private String imageUrl;

    // (선택) 햄버거, 음료, 사이드 등을 구분하는 카테고리
    private String category;

    public Menu(MenuDto.CreateRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.imageUrl = request.getImageUrl();
        this.category = request.getCategory();
    }

    public void update(MenuDto.UpdateRequest request) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.imageUrl = request.getImageUrl();
        this.category = request.getCategory();
    }
}