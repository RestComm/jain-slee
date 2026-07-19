/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A dependency-free, native-image-friendly HTML template helper — no Qute/JSP
 * engine to avoid extra runtime weight in the example. Mustache-ish syntax:
 *
 * <ul>
 *   <li>{@code {{key}}}   — HTML-escaped substitution</li>
 *   <li>{@code {{{key}}}} — raw substitution (already-trusted HTML, e.g. the
 *       Markdown-rendered article body or a pre-built list fragment)</li>
 * </ul>
 *
 * Templates live on the classpath under {@code /templates/<name>.html} and are
 * cached after first load.
 */
public final class Templates {

    private static final ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>();

    private Templates() {
    }

    /** Render {@code templates/<name>.html} against {@code model}. */
    public static String render(String name, Map<String, String> model) {
        String tpl = CACHE.computeIfAbsent(name, Templates::load);
        String out = tpl;
        for (Map.Entry<String, String> e : model.entrySet()) {
            String value = e.getValue() == null ? "" : e.getValue();
            out = out.replace("{{{" + e.getKey() + "}}}", value);
            out = out.replace("{{" + e.getKey() + "}}", escape(value));
        }
        // Drop any placeholders the model didn't supply.
        out = out.replaceAll("\\{\\{\\{?[a-zA-Z0-9_]+}?}}", "");
        return out;
    }

    private static String load(String name) {
        String path = "/templates/" + name + ".html";
        try (InputStream in = Templates.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("template not found: " + path);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read " + path, e);
        }
    }

    /** Minimal HTML-entity escaping for interpolated text. */
    public static String escape(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder b = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&' -> b.append("&amp;");
                case '<' -> b.append("&lt;");
                case '>' -> b.append("&gt;");
                case '"' -> b.append("&quot;");
                case '\'' -> b.append("&#39;");
                default -> b.append(c);
            }
        }
        return b.toString();
    }
}
