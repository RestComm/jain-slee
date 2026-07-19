/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.render;

import java.text.Normalizer;
import java.util.Locale;

/** Turns a title into a URL-safe slug, diacritics-aware (Vietnamese included). */
public final class SlugUtils {

    private SlugUtils() {
    }

    /**
     * {@code "Chào Thế Giới!"} → {@code "chao-the-gioi"}.
     *
     * @param title source title (may be null/blank)
     * @return a lowercase, ASCII, hyphen-separated slug (never blank)
     */
    public static String generate(String title) {
        if (title == null || title.isBlank()) {
            return "untitled";
        }
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace('đ', 'd').replace('Đ', 'D');
        String slug = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
        return slug.isBlank() ? "untitled" : slug;
    }
}
