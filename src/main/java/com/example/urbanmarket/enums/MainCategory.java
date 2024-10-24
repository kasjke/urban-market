package com.example.urbanmarket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainCategory {
    ANIME("Anime"),
    MANGA_BOOKS("MangaBooks"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    BEAUTY_AND_HEALTH("Beauty and Health"),
    SPORTS("Sports"),
    OTHER("Other");

    private final String displayName;


}
