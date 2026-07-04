/*
 * micro-jainslee 1.1.0 -- example application (example-embedded-j25)
 */

package com.example.ussddemo;

import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point for the embedded (plain-Java 25) USSD gateway demo.
 *
 * <p>Boots {@link MicroSleeContainer}, installs HTTP + gRPC resource
 * adaptors from vendor-ras, and blocks until shutdown.
 */
public final class EmbeddedUssdMain {

    private static final Logger LOG = LogManager.getLogger(EmbeddedUssdMain.class);

    private static volatile MicroSleeContainer container;
    private static volatile EmbeddedUssdBootstrap bootstrap;
    private static volatile UssdDemoRuntime runtime;
    private static volatile Thread shutdownHook;

    public static void main(String[] args) throws Exception {
        int httpPort = args.length > 0 ? Integer.parseInt(args[0]) : defaultHttpPort();
        String grpcHost = env("ussd.demo.grpc.host", "127.0.0.1");
        int grpcPort = envInt("ussd.demo.grpc.port", 9090);

        MicroSleeConfiguration configuration = MicroSleeConfiguration.builder()
                .eventRouterBufferSize(envInt("microjainslee.buffer-size", 2048))
                .preferVirtualThreads(envBool("microjainslee.prefer-virtual-threads", true))
                .sbbPoolMin(envInt("microjainslee.sbb-pool-min", 16))
                .sbbPoolMax(envInt("microjainslee.sbb-pool-max", 4096))
                .sbbPerVirtualThread(envBool("microjainslee.sbb-per-virtual-thread", true))
                .txEnabled(envBool("microjainslee.tx-enabled", false))
                .build();

        container = new MicroSleeContainer(configuration);
        runtime = new UssdDemoRuntime();
        bootstrap = new EmbeddedUssdBootstrap(container);

        bootstrap.bindInitialEventSelector();
        container.start();
        bootstrap.install(httpPort, grpcHost, grpcPort);

        LOG.info("MicroSleeContainer started (bufferSize={})", configuration.getEventRouterBufferSize());
        LOG.info("Embedded USSD gateway listening on http://127.0.0.1:{}", httpPort);
        LOG.info("gRPC menu backend configured at {}:{}", grpcHost, grpcPort);

        CountDownLatch shutdownLatch = new CountDownLatch(1);
        if (shutdownHook == null) {
            shutdownHook = new Thread(() -> {
                LOG.info("Shutdown hook fired -- stopping embedded USSD demo");
                try { bootstrap.shutdown(); } catch (Exception e) { LOG.warn("Bootstrap shutdown failed", e); }
                container.stop();
                shutdownLatch.countDown();
            }, "embedded-shutdown-hook");
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
        shutdownLatch.await();
    }

    public static MicroSleeContainer container() { return require(container, "container"); }
    public static EmbeddedUssdBootstrap bootstrap() { return require(bootstrap, "bootstrap"); }
    public static UssdDemoRuntime runtime() { return require(runtime, "runtime"); }

    private static <T> T require(T ref, String name) {
        if (ref == null) throw new IllegalStateException("EmbeddedUssdMain not started yet: " + name);
        return ref;
    }

    private static int defaultHttpPort() { return envInt("ussd.demo.http.port", 8082); }
    private static String env(String name, String dflt) { String v = System.getProperty(name); return v == null || v.isEmpty() ? dflt : v; }
    private static int envInt(String name, int dflt) { String v = System.getProperty(name); return v == null || v.isEmpty() ? dflt : Integer.parseInt(v); }
    private static boolean envBool(String name, boolean dflt) { String v = System.getProperty(name); return v == null || v.isEmpty() ? dflt : Boolean.parseBoolean(v); }

    private EmbeddedUssdMain() {}
}
