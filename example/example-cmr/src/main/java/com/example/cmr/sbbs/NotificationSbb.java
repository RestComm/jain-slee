/*
 * micro-jainslee example :: CMR
 */
package com.example.cmr.sbbs;

import com.example.cmr.events.article.ArticlePublishedEvent;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.SbbAnnotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Downstream reactor to {@link ArticlePublishedEvent} — the fan-out leg of the
 * pipeline. In a real CMR this would notify subscribers, warm the CDN cache, or
 * ping a webhook; here it logs and counts so the flow (and the second SBB hop)
 * is observable.
 */
@SbbAnnotation(name = "NotificationSbb", vendor = "cmr", version = "1.0")
public final class NotificationSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(NotificationSbb.class);

    private static final AtomicLong PUBLISHED = new AtomicLong();

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof ArticlePublishedEvent e) {
            long total = PUBLISHED.incrementAndGet();
            LOG.info("[notify] published '{}' (/news/{}) by {} — total published {}",
                    e.title(), e.slug(), e.initiator(), total);
        }
    }

    /** Total published-notifications observed — surfaced on the dashboard. */
    public static long publishedCount() {
        return PUBLISHED.get();
    }
}
