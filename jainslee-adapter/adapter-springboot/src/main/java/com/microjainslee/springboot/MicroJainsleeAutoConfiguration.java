/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.springboot;

import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.RaEndpointPort;
import com.microjainslee.api.TimerPort;
import com.microjainslee.core.EventRouter;
import com.microjainslee.core.InMemoryActivityContextNamingFacility;
import com.microjainslee.core.MicroSleeConfiguration;
import com.microjainslee.core.MicroSleeContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(MicroJainsleeProperties.class)
public class MicroJainsleeAutoConfiguration {

    private static final Logger LOG = LogManager.getLogger(MicroJainsleeAutoConfiguration.class);

    @Bean
    public MicroSleeConfiguration microSleeConfiguration(MicroJainsleeProperties props) {
        MicroSleeConfiguration cfg = MicroSleeConfiguration.builder()
                .eventRouterBufferSize(props.getEventRouter().getBufferSize())
                .preferVirtualThreads(props.getEventRouter().isPreferVirtualThreads())
                .sbbPoolMin(props.getSbbPool().getMin())
                .sbbPoolMax(props.getSbbPool().getMax())
                .sbbPerVirtualThread(props.getSbbPool().isPerVirtualThread())
                .build();
        LOG.info("Built MicroSleeConfiguration: bufferSize={}, preferVT={}, sbbPool={}-{}, perVT={}",
                cfg.getEventRouterBufferSize(), cfg.isPreferVirtualThreads(),
                cfg.getSbbPoolMin(), cfg.getSbbPoolMax(), cfg.isSbbPerVirtualThread());
        return cfg;
    }

    @Bean
    public MicroSleeContainer microSleeContainer(MicroSleeConfiguration cfg) {
        LOG.info("Creating MicroSleeContainer bean");
        return new MicroSleeContainer(cfg);
    }

    @Bean
    public InMemoryActivityContextNamingFacility microJainsleeAcnf() {
        LOG.debug("Creating InMemoryActivityContextNamingFacility bean");
        return new InMemoryActivityContextNamingFacility();
    }

    @Bean
    public EventRouter microJainsleeEventRouter(MicroSleeContainer container) {
        LOG.debug("Exposing EventRouter bean (state={})", container.getState());
        return container.getEventRouter();
    }

    @Bean
    public TimerPort microJainsleeTimerPort(MicroSleeContainer container) {
        LOG.debug("Exposing TimerPort bean (state={})", container.getState());
        return container.getTimerPort();
    }

    @Bean
    public MicroJainsleeLifecycle microJainsleeLifecycle(MicroSleeContainer container,
                                                          MicroJainsleeProperties props,
                                                          List<RaEndpointPort> raEndpoints,
                                                          List<RaCommandPort> raCommands) {
        LOG.info("Creating MicroJainsleeLifecycle bean (raEndpoints={}, raCommands={})",
                raEndpoints != null ? raEndpoints.size() : 0,
                raCommands != null ? raCommands.size() : 0);
        return new MicroJainsleeLifecycle(container, props, raEndpoints, raCommands);
    }
}