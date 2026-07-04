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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "microjainslee")
public class MicroJainsleeProperties {
    private final EventRouter eventRouter = new EventRouter();
    private final SbbPool sbbPool = new SbbPool();
    private final Deployment deployment = new Deployment();
    private Map<String, String> eventToSbbMappings = new LinkedHashMap<>();
    private List<String> raRegistrations = new ArrayList<>();
    public EventRouter getEventRouter() { return eventRouter; }
    public SbbPool getSbbPool() { return sbbPool; }
    public Deployment getDeployment() { return deployment; }
    public Map<String, String> getEventToSbbMappings() { return eventToSbbMappings; }
    public void setEventToSbbMappings(Map<String, String> v) { this.eventToSbbMappings = v; }
    public List<String> getRaRegistrations() { return raRegistrations; }
    public void setRaRegistrations(List<String> v) { this.raRegistrations = v; }
    public static class EventRouter {
        private int bufferSize = 1024;
        private boolean preferVirtualThreads = true;
        public int getBufferSize() { return bufferSize; }
        public void setBufferSize(int v) { this.bufferSize = v; }
        public boolean isPreferVirtualThreads() { return preferVirtualThreads; }
        public void setPreferVirtualThreads(boolean v) { this.preferVirtualThreads = v; }
    }
    public static class SbbPool {
        private int min = 16;
        private int max = 1024;
        private boolean perVirtualThread = true;
        public int getMin() { return min; }
        public void setMin(int v) { this.min = v; }
        public int getMax() { return max; }
        public void setMax(int v) { this.max = v; }
        public boolean isPerVirtualThread() { return perVirtualThread; }
        public void setPerVirtualThread(boolean v) { this.perVirtualThread = v; }
    }
    public static class Deployment {
        private String scanPaths = "";
        private boolean autoRegister = false;
        public String getScanPaths() { return scanPaths; }
        public void setScanPaths(String v) { this.scanPaths = v; }
        public boolean isAutoRegister() { return autoRegister; }
        public void setAutoRegister(boolean v) { this.autoRegister = v; }
    }
}