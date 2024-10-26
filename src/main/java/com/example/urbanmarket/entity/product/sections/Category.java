package com.example.urbanmarket.entity.product.sections;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    ANIME("Anime"),
    MANGA_BOOKS("MangaBooks"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    BEAUTY_AND_HEALTH("Beauty and Health"),
    SPORTS("Sports"),
    OTHER("Other");

    private final String displayName;
}
