package com.example.urbanmarket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum SubCategory {

    FIGURES("Figures", MainCategory.ANIME),
    PILLOWS("Pillows", MainCategory.ANIME),
    CUPS("Cups", MainCategory.ANIME),
    MANGA("Manga", MainCategory.ANIME),
    NOVELS("Novels", MainCategory.ANIME),

    HOODIES("Hoodies", MainCategory.CLOTHING),
    T_SHIRTS("T-Shirts", MainCategory.CLOTHING),
    DRESSES("Dresses", MainCategory.CLOTHING),
    SHOES("Shoes", MainCategory.CLOTHING),
    SKIRTS("Skirts", MainCategory.CLOTHING),
    SHORTS("Shorts", MainCategory.CLOTHING),
    SUITS("Suits", MainCategory.CLOTHING),
    MANGA_OTHER("Other", MainCategory.CLOTHING),

    HOME_ACCESSORIES("Home Accessories", MainCategory.ACCESSORIES),
    HAIR_ACCESSORIES("Hair Accessories", MainCategory.ACCESSORIES),
    BODY_ACCESSORIES("Body Accessories", MainCategory.ACCESSORIES),
    JEWELRY("Jewelry", MainCategory.ACCESSORIES),
    ACCESSORIES_OTHER("Other", MainCategory.ACCESSORIES),

    COSMETICS("Cosmetics", MainCategory.BEAUTY_AND_HEALTH),
    HAIR_CARE("Hair Care", MainCategory.BEAUTY_AND_HEALTH),
    FACE_CARE("Face Care", MainCategory.BEAUTY_AND_HEALTH),
    OILS("Oils", MainCategory.BEAUTY_AND_HEALTH),
    VITAMINS("Vitamins", MainCategory.BEAUTY_AND_HEALTH),
    BODY_CARE("Body Care", MainCategory.BEAUTY_AND_HEALTH),
    PERFUME("Perfume", MainCategory.BEAUTY_AND_HEALTH),
    BEAUTY_OTHER("Other", MainCategory.BEAUTY_AND_HEALTH);

    private final String displayName;
    private final MainCategory mainCategory;

    public static List<SubCategory> getSubcategoriesForMainCategory(MainCategory mainCategory) {
        return Arrays.stream(values())
                .filter(sub -> sub.getMainCategory() == mainCategory)
                .toList();
    }
}