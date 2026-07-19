/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.bootstrap;

import com.example.cmr.autonomous.AppAiAgent;
import com.example.cmr.autonomous.AppAutonomous;
import com.example.cmr.events.article.ArticleCreatedEvent;
import com.example.cmr.events.article.ArticleDeletedEvent;
import com.example.cmr.events.article.ArticlePublishedEvent;
import com.example.cmr.events.article.ArticleUpdatedEvent;
import com.example.cmr.events.media.FileDeletedEvent;
import com.example.cmr.events.media.FileUploadedEvent;
import com.example.cmr.events.user.UserLoginEvent;
import com.example.cmr.events.user.UserSessionExpiredEvent;
import com.example.cmr.http.MonitorHandler;
import com.example.cmr.http.SiteHandler;
import com.example.cmr.model.AdminUser;
import com.example.cmr.model.Article;
import com.example.cmr.model.ArticleStatus;
import com.example.cmr.model.Category;
import com.example.cmr.ports.ArticleRepository;
import com.example.cmr.ports.MarkdownRenderer;
import com.example.cmr.ports.MediaRepository;
import com.example.cmr.render.FlexmarkRenderer;
import com.example.cmr.render.SlugUtils;
import com.example.cmr.repo.InMemoryArticleRepository;
import com.example.cmr.repo.InMemoryMediaRepository;
import com.example.cmr.sbbs.ArticleSbb;
import com.example.cmr.sbbs.HttpGatewaySbb;
import com.example.cmr.sbbs.MediaSbb;
import com.example.cmr.sbbs.NotificationSbb;
import com.example.cmr.sbbs.UserSessionSbb;
import com.example.cmr.storage.LocalStorageRa;
import com.example.cmr.auth.JwtAuthRa;
import com.example.cmr.telemetry.AppTelemetry;

import com.microjainslee.ai.AIAgentConfig;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.core.MicroSleeContainer;
import com.microjainslee.ra.httpserver.HttpServerRaEndpoint;
import com.microjainslee.ra.httpserver.HttpServerResourceAdaptor;
import com.microjainslee.ra.httpserver.events.HttpWebRequestEvent;
import com.microjainslee.telemetry.TelemetryPort;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import io.quarkus.runtime.StartupEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * CMR bootstrap — the single Quarkus wiring point. Same drop-in template as the
 * other examples: {@code telemetry/} + {@code autonomous/} (incl. the AI agent
 * configured from {@code microjainslee.ai.*}) are optional legs on top of the
 * core container, then the CMR business layer is wired:
 *
 * <ul>
 *   <li>storage + auth resource adaptors (3-port, registered with the container)</li>
 *   <li>four content SBBs (Article / Media / UserSession / Notification)</li>
 *   <li>{@code mapEventToSbb} for every content-lifecycle event</li>
 *   <li><b>one</b> {@code ra-http-server} on {@code cmr.http.port} + one
 *       {@link HttpGatewaySbb} that routes every request to the monitor or
 *       site handler — no app-level Vert.x anywhere</li>
 * </ul>
 *
 * <p><b>Architecture rule:</b> the app never creates a Vert.x instance or an
 * HTTP server. Vert.x lives only inside {@code ra-http-server}; the whole web
 * surface (public site, admin, telemetry dashboard, autonomous health, AI REST)
 * rides that single RA through the gateway SBB.</p>
 *
 * <p>100% Quarkus: CDI lifecycle + MicroProfile config, no Spring, no embedded
 * main.</p>
 */
@ApplicationScoped
public final class CmrBootstrap {

    private static final Logger LOG = LogManager.getLogger(CmrBootstrap.class);

    @Inject
    MicroSleeContainer container;

    @Inject
    org.eclipse.microprofile.config.Config mpConfig;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.site-name", defaultValue = "MicroNews")
    String siteName;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.http.port", defaultValue = "8080")
    int httpPort;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.upload-dir", defaultValue = "cmr-data/media")
    String uploadDir;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.session.cookie", defaultValue = "cmr_session")
    String sessionCookie;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.session.ttl-seconds", defaultValue = "3600")
    long sessionTtl;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.admin.username", defaultValue = "admin")
    String adminUser;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.admin.password", defaultValue = "admin")
    String adminPassword;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.admin.display-name", defaultValue = "Administrator")
    String adminDisplay;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.auth.secret",
            defaultValue = "cmr-dev-secret-change-me-please-32bytes")
    String authSecret;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "cmr.categories",
            defaultValue = "news:Tin tức,technology:Công nghệ,business:Kinh doanh,life:Đời sống")
    String categoriesSpec;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "microjainslee.telemetry.enabled", defaultValue = "true")
    boolean telemetryEnabled;

    @org.eclipse.microprofile.config.inject.ConfigProperty(name = "microjainslee.autonomous.enabled", defaultValue = "true")
    boolean autonomousEnabled;

    // ── drop-in optional modules (same trio as helloworld) ──
    private final AppTelemetry appTelemetry = new AppTelemetry();
    private final AppAutonomous appAutonomous = new AppAutonomous();
    private final AppAiAgent appAiAgent = new AppAiAgent();

    // ── CMR runtime collaborators ──
    private final ArticleRepository articles = new InMemoryArticleRepository();
    private final MediaRepository media = new InMemoryMediaRepository();
    private final MarkdownRenderer renderer = new FlexmarkRenderer();
    private LocalStorageRa storage;
    private JwtAuthRa auth;
    private HttpServerRaEndpoint httpRa;
    private volatile boolean started;

    /**
     * Eager init on Quarkus startup. Observing {@link StartupEvent} forces creation
     * of this otherwise-lazy {@code @ApplicationScoped} bean so RA wiring always runs
     * (same pattern as helloworld / ussdgw / sip examples).
     */
    void onStart(@Observes StartupEvent ev) {
        LOG.info("CMR bootstrap triggered by StartupEvent");
        init();
    }

    private void init() {
        if (started) {
            return;
        }
        started = true;
        if (container.getState() != MicroSleeContainer.State.STARTED) {
            container.start();
        }

        TelemetryPort telemetry = null;
        if (telemetryEnabled) {
            telemetry = appTelemetry.install(container);
        } else {
            LOG.info("telemetry disabled (microjainslee.telemetry.enabled=false)");
        }

        Supplier<String> healthJson = null;
        if (autonomousEnabled && telemetry != null) {
            appAutonomous.install(container, telemetry);
            healthJson = appAutonomous::healthJson;
        } else if (autonomousEnabled) {
            LOG.warn("autonomous skipped: needs telemetry as data source");
        }

        // AI agent — reads microjainslee.ai.* from application.properties.
        if (telemetry != null) {
            AIAgentConfig aiConfig = AIAgentConfig.fromProperties(key ->
                    mpConfig.getOptionalValue(key, String.class).orElse(null));
            appAiAgent.install(aiConfig, container, telemetry,
                    autonomousEnabled ? appAutonomous.guardian() : null);
        }

        // The observability surface (dashboard + telemetry/autonomous/AI APIs)
        // rides the same ra-http-server as the site. Null when telemetry is off.
        MonitorHandler monitor = telemetry == null ? null
                : new MonitorHandler(telemetry, healthJson, appAiAgent.engine());

        wireCmr(container, monitor);
        LOG.info("CMR ready — site '{}' on http://localhost:{}  (admin: /admin, dashboard: /telemetry)",
                siteName, httpPort);
    }

    private void wireCmr(MicroSleeContainer container, MonitorHandler monitor) {
        List<Category> categories = parseCategories(categoriesSpec);

        // storage + auth resource adaptors (3-port).
        storage = new LocalStorageRa(Path.of(uploadDir), "/media");
        container.registerRa(storage, storage);

        auth = new JwtAuthRa(authSecret, Map.of(adminUser,
                new AdminUser(adminUser, JwtAuthRa.hash(adminUser, adminPassword), adminDisplay)));
        container.registerRa(auth, auth);

        // Fire CMR write events into the SLEE pipeline (they land on the mapped
        // content SBBs). A CMS is write-through-events, read-direct.
        Consumer<SleeEvent> sink = ev ->
                container.routeEvent(ev, container.createActivityContext("cmr-http-" + UUID.randomUUID()));

        SiteHandler site = new SiteHandler(siteName, sessionCookie, sessionTtl, categories,
                articles, media, storage, auth, sink);

        // content SBBs + the single HTTP gateway SBB.
        container.registerSbbType(ArticleSbb.class,
                () -> new ArticleSbb(articles, renderer, container));
        container.registerSbbType(MediaSbb.class,
                () -> new MediaSbb(storage, media, container));
        container.registerSbbType(UserSessionSbb.class, UserSessionSbb::new);
        container.registerSbbType(NotificationSbb.class, NotificationSbb::new);
        container.registerSbbType(HttpGatewaySbb.class,
                () -> new HttpGatewaySbb(monitor, site));
        container.createIesDispatcher();

        // content-lifecycle event → SBB routing.
        container.mapEventToSbb(ArticleCreatedEvent.class, "ArticleSbb");
        container.mapEventToSbb(ArticleUpdatedEvent.class, "ArticleSbb");
        container.mapEventToSbb(ArticleDeletedEvent.class, "ArticleSbb");
        container.mapEventToSbb(FileUploadedEvent.class, "MediaSbb");
        container.mapEventToSbb(FileDeletedEvent.class, "MediaSbb");
        container.mapEventToSbb(UserLoginEvent.class, "UserSessionSbb");
        container.mapEventToSbb(UserSessionExpiredEvent.class, "UserSessionSbb");
        container.mapEventToSbb(ArticlePublishedEvent.class, "NotificationSbb");
        // every inbound HTTP request → the one gateway SBB.
        container.mapEventToSbb(HttpWebRequestEvent.class, "HttpGatewaySbb");

        seedSampleContent(categories);

        // The single HTTP ingress RA — Vert.x lives here and only here.
        HttpServerResourceAdaptor ra = new HttpServerResourceAdaptor();
        ra.setPort(httpPort);
        ra.setHost("0.0.0.0");
        httpRa = new HttpServerRaEndpoint(ra);
        httpRa.setPort(httpPort);
        container.registerRa(httpRa, httpRa);
        LOG.info("[cmr] ra-http-server listening on :{} — site /, admin /admin, dashboard /telemetry",
                httpPort);
    }

    /** Seed one welcome article so the public tabs are not empty on first run. */
    private void seedSampleContent(List<Category> categories) {
        if (articles.count() > 0 || categories.isEmpty()) {
            return;
        }
        String md = "Chào mừng đến với **" + siteName + "** — một CMS chạy trên "
                + "*micro-jainslee* như một content lifecycle engine.\n\n"
                + "## Cách hoạt động\n\n"
                + "- Bạn đăng bài ở `/admin` → phát `ArticleCreatedEvent`\n"
                + "- `ArticleSbb` render Markdown → HTML, lưu, rồi phát `ArticlePublishedEvent`\n"
                + "- `NotificationSbb` nhận sự kiện downstream (thông báo, cache)\n\n"
                + "> SBB là content handler, Event là vòng đời nội dung, RA là HTTP/storage/auth.\n";
        Instant now = Instant.now();
        String cat = categories.get(0).slug();
        Article welcome = new Article(
                java.util.UUID.randomUUID().toString(),
                SlugUtils.generate("Chào mừng đến với " + siteName),
                "Chào mừng đến với " + siteName,
                cat, List.of("micro-jainslee", "cms"),
                md, renderer.render(md), null, adminUser,
                ArticleStatus.PUBLISHED, now, now, now);
        articles.save(welcome);
        LOG.info("[cmr] seeded welcome article in category '{}'", cat);
    }

    private static List<Category> parseCategories(String spec) {
        List<Category> out = new ArrayList<>();
        int order = 0;
        for (String part : spec.split(",")) {
            String p = part.strip();
            if (p.isEmpty()) {
                continue;
            }
            int colon = p.indexOf(':');
            String slug = colon > 0 ? p.substring(0, colon).strip() : p;
            String name = colon > 0 ? p.substring(colon + 1).strip() : p;
            out.add(new Category(slug, name, order++));
        }
        return out;
    }

    @PreDestroy
    void shutdown() {
        if (httpRa != null) {
            httpRa.deactivate();
        }
        appAiAgent.close();
        appAutonomous.close();
        appTelemetry.close();
        if (container.getState() == MicroSleeContainer.State.STARTED) {
            container.stop();
        }
    }
}
