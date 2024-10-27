package com.example.urbanmarket.entity.product.sections;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
