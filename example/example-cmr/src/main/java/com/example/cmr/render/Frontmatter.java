/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.render;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A minimal YAML-front-matter splitter for uploaded Markdown files:
 *
 * <pre>
 * ---
 * title: Hello World
 * category: technology
 * tags: java, slee, cms
 * ---
 * # Body starts here
 * </pre>
 *
 * Only flat {@code key: value} lines are supported — enough to drive an
 * article's metadata without pulling in a YAML engine.
 *
 * @param meta parsed front-matter keys (empty if none present)
 * @param body the Markdown body after the front-matter block
 */
public record Frontmatter(Map<String, String> meta, String body) {

    private static final String FENCE = "---";

    /** Split a raw upload into {@code (meta, body)}. */
    public static Frontmatter parse(String raw) {
        if (raw == null) {
            return new Frontmatter(Map.of(), "");
        }
        String text = raw.stripLeading();
        if (!text.startsWith(FENCE)) {
            return new Frontmatter(Map.of(), raw);
        }
        String[] lines = text.split("\r?\n");
        Map<String, String> meta = new LinkedHashMap<>();
        int i = 1; // skip opening fence
        for (; i < lines.length; i++) {
            if (lines[i].strip().equals(FENCE)) {
                i++; // consume closing fence
                break;
            }
            int colon = lines[i].indexOf(':');
            if (colon > 0) {
                String key = lines[i].substring(0, colon).strip().toLowerCase();
                String val = lines[i].substring(colon + 1).strip();
                meta.put(key, val);
            }
        }
        StringBuilder body = new StringBuilder();
        for (; i < lines.length; i++) {
            body.append(lines[i]).append('\n');
        }
        return new Frontmatter(meta, body.toString().stripLeading());
    }

    /** Comma-split a value into a trimmed list (e.g. {@code tags}). */
    public List<String> list(String key) {
        String v = meta.get(key);
        if (v == null || v.isBlank()) {
            return List.of();
        }
        return List.of(v.split("\\s*,\\s*"));
    }

    /** Value or a fallback. */
    public String get(String key, String fallback) {
        String v = meta.get(key);
        return v == null || v.isBlank() ? fallback : v;
    }
}
