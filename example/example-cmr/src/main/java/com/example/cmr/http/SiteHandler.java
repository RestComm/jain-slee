/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.http;

import com.example.cmr.events.article.ArticleCreatedEvent;
import com.example.cmr.events.article.ArticleDeletedEvent;
import com.example.cmr.events.article.ArticleUpdatedEvent;
import com.example.cmr.events.media.FileUploadedEvent;
import com.example.cmr.events.user.UserLoginEvent;
import com.example.cmr.model.Article;
import com.example.cmr.model.ArticleStatus;
import com.example.cmr.model.Category;
import com.example.cmr.model.MediaFile;
import com.example.cmr.ports.ArticleRepository;
import com.example.cmr.ports.AuthPort;
import com.example.cmr.ports.MediaRepository;
import com.example.cmr.ports.StoragePort;
import com.example.cmr.sbbs.NotificationSbb;
import com.example.cmr.sbbs.UserSessionSbb;
import com.example.cmr.web.Templates;

import com.microjainslee.api.SleeEvent;
import com.microjainslee.ra.httpserver.events.HttpUpload;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The whole CMR web surface — admin back office and public reader site — as a
 * pure function {@code (HttpWebRequestEvent) → HttpReply}. It replaces the old
 * Vert.x {@code AdminRouter}/{@code PublicRouter}: no {@code RoutingContext}, no
 * {@code Router}, no embedded HTTP server. Requests arrive already parsed by
 * {@code ra-http-server} (form fields, cookies, uploads); replies (HTML,
 * redirects with {@code Set-Cookie}, binary media) travel back as an
 * {@link HttpReply} that {@code HttpGatewaySbb} turns into an
 * {@code HttpResponseExCommand}.
 *
 * <p>Writes (create/update/delete/upload/login) are fired as CMR events into
 * the SLEE pipeline via the {@code sink}; reads render straight from the
 * repositories — a CMS is write-through-events, read-direct.</p>
 */
public final class SiteHandler {

    private static final Logger LOG = LogManager.getLogger(SiteHandler.class);
    private static final DateTimeFormatter LIST_DATE =
            DateTimeFormatter.ofPattern("d MMM HH:mm", new Locale("vi")).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter ARTICLE_DATE =
            DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("vi")).withZone(ZoneId.systemDefault());

    private final String siteName;
    private final String sessionCookie;
    private final long sessionTtlSeconds;
    private final List<Category> categories;
    private final Map<String, String> categoryNames;
    private final ArticleRepository articles;
    private final MediaRepository media;
    private final StoragePort storage;
    private final AuthPort auth;
    private final Consumer<SleeEvent> sink;

    public SiteHandler(String siteName, String sessionCookie, long sessionTtlSeconds,
                       List<Category> categories, ArticleRepository articles, MediaRepository media,
                       StoragePort storage, AuthPort auth, Consumer<SleeEvent> sink) {
        this.siteName = siteName;
        this.sessionCookie = sessionCookie;
        this.sessionTtlSeconds = sessionTtlSeconds;
        this.categories = List.copyOf(categories);
        this.articles = articles;
        this.media = media;
        this.storage = storage;
        this.auth = auth;
        this.sink = sink;
        this.categoryNames = categories.stream()
                .collect(Collectors.toMap(Category::slug, Category::name, (a, b) -> a, LinkedHashMap::new));
    }

    /** Route a request to the right page. Always answers (404 for unknown paths). */
    public HttpReply handle(HttpWebRequestEvent e) {
        String path = e.getPath();
        boolean get = e.getMethod().equalsIgnoreCase("GET");
        boolean post = e.getMethod().equalsIgnoreCase("POST");

        // ── admin back office ──
        if (path.equals("/admin")) {
            return HttpReply.redirect("/admin/dashboard");
        }
        if (path.equals("/admin/login")) {
            return get ? renderLogin(null) : doLogin(e);
        }
        if (path.equals("/admin/logout")) {
            return HttpReply.redirect("/admin/login")
                    .withHeader("Set-Cookie", expiredCookie());
        }
        if (path.startsWith("/admin")) {
            Optional<String> user = currentUser(e);
            if (user.isEmpty()) {
                return HttpReply.redirect("/admin/login");
            }
            return admin(e, path, user.get(), get, post);
        }

        // ── public reader site ──
        if (get && path.equals("/")) {
            return home(null);
        }
        if (get && path.startsWith("/category/")) {
            return home(tail(path, "/category/"));
        }
        if (get && path.startsWith("/news/")) {
            return article(tail(path, "/news/"));
        }
        if (get && path.startsWith("/media/")) {
            return serveMedia(tail(path, "/media/"));
        }
        return HttpReply.notFound();
    }

    // ── admin (authenticated) ──

    private HttpReply admin(HttpWebRequestEvent e, String path, String user,
                            boolean get, boolean post) {
        if (get && path.equals("/admin/dashboard")) {
            return dashboard();
        }
        if (get && path.equals("/admin/article/new")) {
            return editor(null);
        }
        if (get && path.startsWith("/admin/article/") && path.endsWith("/edit")) {
            String id = between(path, "/admin/article/", "/edit");
            return editor(articles.findById(id).orElse(null));
        }
        if (post && path.equals("/admin/article")) {
            return saveArticle(e, user);
        }
        if (post && path.startsWith("/admin/article/") && path.endsWith("/delete")) {
            String id = between(path, "/admin/article/", "/delete");
            return deleteArticle(id, user);
        }
        if (post && path.equals("/admin/upload-md")) {
            return uploadMarkdown(e, user);
        }
        return HttpReply.notFound();
    }

    // ── auth ──

    private Optional<String> currentUser(HttpWebRequestEvent e) {
        String token = e.getCookie(sessionCookie);
        return token == null ? Optional.empty() : auth.validate(token);
    }

    private HttpReply doLogin(HttpWebRequestEvent e) {
        String username = e.getFormAttribute("username");
        String password = e.getFormAttribute("password");
        Optional<String> display = auth.verify(username, password);
        if (display.isEmpty()) {
            LOG.info("[admin] failed login user={}", username);
            return renderLogin("Sai tên đăng nhập hoặc mật khẩu.");
        }
        String token = auth.issueToken(username, sessionTtlSeconds);
        String sessionId = UUID.randomUUID().toString();
        sink.accept(new UserLoginEvent(username, sessionId, remoteIp(e), sessionTtlSeconds));
        return HttpReply.redirect("/admin/dashboard")
                .withHeader("Set-Cookie", sessionCookieHeader(token, sessionTtlSeconds));
    }

    private HttpReply renderLogin(String error) {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("siteName", siteName);
        m.put("error", error == null ? "" : "<div class=\"err\">" + Templates.escape(error) + "</div>");
        m.put("hint", "admin / admin (demo — đổi trong application.properties)");
        return HttpReply.html(Templates.render("admin-login", m));
    }

    // ── admin pages ──

    private HttpReply dashboard() {
        List<Article> all = articles.findAll();
        long published = all.stream().filter(Article::isPublic).count();

        String tiles = tile(String.valueOf(all.size()), "Tổng bài")
                + tile(String.valueOf(published), "Đã xuất bản")
                + tile(String.valueOf(media.count()), "Media")
                + tile(String.valueOf(NotificationSbb.publishedCount()), "Notify (SBB)")
                + tile(String.valueOf(UserSessionSbb.activeSessions()), "Phiên đăng nhập");

        StringBuilder rows = new StringBuilder();
        for (Article a : all) {
            rows.append("<tr>")
                .append("<td><a href=\"/admin/article/").append(Templates.escape(a.id()))
                    .append("/edit\">").append(Templates.escape(a.title())).append("</a></td>")
                .append("<td>").append(Templates.escape(a.categorySlug())).append("</td>")
                .append("<td><span class=\"pill ").append(a.status()).append("\">")
                    .append(a.status()).append("</span></td>")
                .append("<td>").append(LIST_DATE.format(a.updatedAt())).append("</td>")
                .append("<td><form method=\"post\" action=\"/admin/article/")
                    .append(Templates.escape(a.id())).append("/delete\" ")
                    .append("onsubmit=\"return confirm('Xoá bài này?')\">")
                    .append("<button style=\"background:none;border:none;color:#c8102e;cursor:pointer\">Xoá</button>")
                    .append("</form></td>")
                .append("</tr>");
        }
        if (all.isEmpty()) {
            rows.append("<tr><td colspan=\"5\" style=\"color:#8a8aa0;text-align:center;padding:34px\">"
                    + "Chưa có bài viết. Tạo bài mới hoặc upload .md.</td></tr>");
        }

        Map<String, String> m = new LinkedHashMap<>();
        m.put("siteName", siteName);
        m.put("tiles", tiles);
        m.put("rows", rows.toString());
        return HttpReply.html(Templates.render("admin-dashboard", m));
    }

    private HttpReply editor(Article existing) {
        boolean edit = existing != null;
        Map<String, String> m = new LinkedHashMap<>();
        m.put("siteName", siteName);
        m.put("heading", edit ? "Chỉnh sửa bài" : "Tạo bài mới");
        m.put("action", "/admin/article");
        m.put("id", edit ? existing.id() : "");
        m.put("title", edit ? existing.title() : "");
        m.put("category", edit ? existing.categorySlug()
                : (categories.isEmpty() ? "news" : categories.get(0).slug()));
        m.put("tags", edit ? String.join(", ", existing.tags()) : "");
        m.put("content", edit ? existing.rawMarkdown() : "");
        m.put("statusOptions", statusOptions(edit ? existing.status() : ArticleStatus.PUBLISHED));
        return HttpReply.html(Templates.render("admin-editor", m));
    }

    // ── admin mutations (fire events) ──

    private HttpReply saveArticle(HttpWebRequestEvent e, String user) {
        String id = e.getFormAttribute("id");
        String title = e.getFormAttribute("title");
        String category = orDefault(e.getFormAttribute("category"), "news");
        List<String> tags = splitTags(e.getFormAttribute("tags"));
        ArticleStatus status = parseStatus(e.getFormAttribute("status"));
        String content = e.getFormAttribute("content");
        Instant now = Instant.now();

        if (id == null || id.isBlank()) {
            Article a = new Article(UUID.randomUUID().toString(), null, title, category, tags,
                    content, "", null, user, status, now, now, now);
            sink.accept(new ArticleCreatedEvent(a, user));
        } else {
            Article prev = articles.findById(id).orElse(null);
            Instant created = prev != null ? prev.createdAt() : now;
            String slug = prev != null ? prev.slug() : null;
            String cover = prev != null ? prev.coverImageId() : null;
            Article a = new Article(id, slug, title, category, tags, content, "", cover,
                    user, status, created, now, now);
            sink.accept(new ArticleUpdatedEvent(id, a, user));
        }
        return HttpReply.redirect("/admin/dashboard");
    }

    private HttpReply deleteArticle(String id, String user) {
        String slug = articles.findById(id).map(Article::slug).orElse("");
        sink.accept(new ArticleDeletedEvent(id, slug, user));
        return HttpReply.redirect("/admin/dashboard");
    }

    private HttpReply uploadMarkdown(HttpWebRequestEvent e, String user) {
        List<HttpUpload> uploads = e.getUploads();
        if (uploads.isEmpty()) {
            return HttpReply.redirect("/admin/dashboard");
        }
        HttpUpload fu = uploads.get(0);
        sink.accept(new FileUploadedEvent(UUID.randomUUID().toString(),
                fu.filename(), orDefault(fu.contentType(), "text/markdown"), fu.content(), user));
        LOG.info("[admin] {} uploaded {}", user, fu.filename());
        return HttpReply.redirect("/admin/dashboard");
    }

    // ── public pages ──

    private HttpReply home(String activeCategory) {
        List<Article> list = activeCategory == null
                ? articles.findPublished()
                : articles.findPublishedByCategory(activeCategory);

        StringBuilder cards = new StringBuilder();
        for (Article a : list) {
            cards.append(card(a));
        }
        Map<String, String> model = new LinkedHashMap<>();
        model.put("siteName", siteName);
        model.put("year", String.valueOf(Year.now().getValue()));
        model.put("tabs", tabs(activeCategory));
        model.put("articles", cards.toString());
        model.put("empty", list.isEmpty()
                ? "<p class=\"empty\">Chưa có bài viết nào được xuất bản.</p>" : "");
        return HttpReply.html(Templates.render("public-home", model));
    }

    private HttpReply article(String slug) {
        Optional<Article> found = articles.findBySlug(slug).filter(Article::isPublic);
        if (found.isEmpty()) {
            return HttpReply.notFound();
        }
        Article a = found.get();
        Map<String, String> model = new LinkedHashMap<>();
        model.put("siteName", siteName);
        model.put("title", a.title());
        model.put("categorySlug", a.categorySlug());
        model.put("categoryName", categoryName(a.categorySlug()));
        model.put("author", a.authorId() == null ? "Ban biên tập" : a.authorId());
        model.put("date", ARTICLE_DATE.format(a.createdAt()));
        model.put("tagsSuffix", a.tags().isEmpty() ? "" : " · " + String.join(", ", a.tags()));
        model.put("cover", coverImg(a));
        model.put("body", a.renderedHtml());
        return HttpReply.html(Templates.render("public-article", model));
    }

    private HttpReply serveMedia(String file) {
        byte[] bytes = storage.read(file);
        if (bytes == null) {
            return HttpReply.notFound();
        }
        return new HttpReply(200, contentType(file), null, bytes,
                Map.of("Cache-Control", "public, max-age=86400"));
    }

    // ── fragment builders ──

    private String tabs(String active) {
        StringBuilder b = new StringBuilder();
        b.append("<a href=\"/\" class=\"").append(active == null ? "active" : "").append("\">Tất cả</a>");
        for (Category c : categories) {
            boolean on = c.slug().equals(active);
            b.append("<a href=\"/category/").append(Templates.escape(c.slug())).append("\" class=\"")
             .append(on ? "active" : "").append("\">").append(Templates.escape(c.name())).append("</a>");
        }
        return b.toString();
    }

    private String card(Article a) {
        String cover = coverUrl(a);
        String coverStyle = cover == null ? "" : " style=\"background-image:url('" + cover + "')\"";
        return "<a class=\"card\" href=\"/news/" + Templates.escape(a.slug()) + "\">"
                + "<div class=\"cover\"" + coverStyle + "></div>"
                + "<div class=\"body\">"
                + "<div class=\"kicker\">" + Templates.escape(categoryName(a.categorySlug())) + "</div>"
                + "<h2>" + Templates.escape(a.title()) + "</h2>"
                + "<p>" + Templates.escape(excerpt(a.renderedHtml())) + "</p>"
                + "</div></a>";
    }

    private String coverImg(Article a) {
        String url = coverUrl(a);
        return url == null ? "" : "<img class=\"cover\" src=\"" + url + "\" alt=\"\">";
    }

    private String coverUrl(Article a) {
        if (a.coverImageId() == null) {
            return null;
        }
        return media.findById(a.coverImageId()).map(MediaFile::publicUrl).orElse(null);
    }

    private String categoryName(String slug) {
        return categoryNames.getOrDefault(slug, slug);
    }

    private String statusOptions(ArticleStatus selected) {
        return Arrays.stream(ArticleStatus.values())
                .map(s -> "<option value=\"" + s + "\"" + (s == selected ? " selected" : "") + ">"
                        + s + "</option>")
                .collect(Collectors.joining());
    }

    // ── static helpers ──

    private String sessionCookieHeader(String token, long maxAge) {
        return sessionCookie + "=" + token + "; Path=/; HttpOnly; SameSite=Lax; Max-Age=" + maxAge;
    }

    private String expiredCookie() {
        return sessionCookie + "=; Path=/; HttpOnly; SameSite=Lax; Max-Age=0";
    }

    private static String tile(String n, String label) {
        return "<div class=\"tile\"><div class=\"n\">" + Templates.escape(n) + "</div>"
                + "<div class=\"l\">" + Templates.escape(label) + "</div></div>";
    }

    private static List<String> splitTags(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(csv.split(",")).map(String::strip).filter(s -> !s.isBlank()).toList();
    }

    private static ArticleStatus parseStatus(String v) {
        if (v == null) {
            return ArticleStatus.DRAFT;
        }
        try {
            return ArticleStatus.valueOf(v.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return ArticleStatus.DRAFT;
        }
    }

    private static String orDefault(String v, String def) {
        return v == null || v.isBlank() ? def : v;
    }

    private static String remoteIp(HttpWebRequestEvent e) {
        String fwd = e.getHeaders().get("X-Forwarded-For");
        return fwd != null && !fwd.isBlank() ? fwd.split(",")[0].strip() : "?";
    }

    private static String excerpt(String html) {
        String text = html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").strip();
        return text.length() > 140 ? text.substring(0, 140) + "…" : text;
    }

    /** Decode a single path segment after {@code prefix} (URL-decoded). */
    private static String tail(String path, String prefix) {
        return decode(path.substring(prefix.length()));
    }

    private static String between(String path, String prefix, String suffix) {
        return decode(path.substring(prefix.length(), path.length() - suffix.length()));
    }

    private static String decode(String s) {
        return java.net.URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String contentType(String file) {
        String f = file.toLowerCase();
        if (f.endsWith(".png")) return "image/png";
        if (f.endsWith(".jpg") || f.endsWith(".jpeg")) return "image/jpeg";
        if (f.endsWith(".gif")) return "image/gif";
        if (f.endsWith(".svg")) return "image/svg+xml";
        if (f.endsWith(".webp")) return "image/webp";
        if (f.endsWith(".pdf")) return "application/pdf";
        return "application/octet-stream";
    }
}
