package com.example.urbanmarket.entity.product.sections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum SubCategory {

    FIGURES("Figures", Category.ANIME),
    PILLOWS("Pillows", Category.ANIME),
    CUPS("Cups", Category.ANIME),
    MANGA("Manga", Category.ANIME),
    NOVELS("Novels", Category.ANIME),

    HOODIES("Hoodies", Category.CLOTHING),
    T_SHIRTS("T-Shirts", Category.CLOTHING),
    DRESSES("Dresses", Category.CLOTHING),
    SHOES("Shoes", Category.CLOTHING),
    SKIRTS("Skirts", Category.CLOTHING),
    SHORTS("Shorts", Category.CLOTHING),
    SUITS("Suits", Category.CLOTHING),
    MANGA_OTHER("Other", Category.CLOTHING),

    HOME_ACCESSORIES("Home Accessories", Category.ACCESSORIES),
    HAIR_ACCESSORIES("Hair Accessories", Category.ACCESSORIES),
    BODY_ACCESSORIES("Body Accessories", Category.ACCESSORIES),
    JEWELRY("Jewelry", Category.ACCESSORIES),
    ACCESSORIES_OTHER("Other", Category.ACCESSORIES),

    COSMETICS("Cosmetics", Category.BEAUTY_AND_HEALTH),
    HAIR_CARE("Hair Care", Category.BEAUTY_AND_HEALTH),
    FACE_CARE("Face Care", Category.BEAUTY_AND_HEALTH),
    OILS("Oils", Category.BEAUTY_AND_HEALTH),
    VITAMINS("Vitamins", Category.BEAUTY_AND_HEALTH),
    BODY_CARE("Body Care", Category.BEAUTY_AND_HEALTH),
    PERFUME("Perfume", Category.BEAUTY_AND_HEALTH),
    BEAUTY_OTHER("Other", Category.BEAUTY_AND_HEALTH);

    private final String displayName;
    private final Category category;

    public static List<SubCategory> getSubcategoriesForMainCategory(Category category) {
        return Arrays.stream(values())
                .filter(sub -> sub.getCategory() == category)
                .toList();
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "displayName='" + displayName + '\'' +
                ", category=" + category.getDisplayName() +
                '}';
    }
}
