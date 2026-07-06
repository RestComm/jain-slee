package com.example.helloworld.spring.rest;

import com.example.helloworld.spring.HelloWorldContext;
import com.microjainslee.telemetry.TelemetryPort;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for the telemetry dashboard API.
 * Serves /api/telemetry/* endpoints consumed by the telemetry GUI.
 */
@RestController
@RequestMapping("/api/telemetry")
public final class TelemetryController {

    private TelemetryPort telemetry() {
        return HelloWorldContext.telemetryPort();
    }

    @GetMapping("/snapshot")
    public TelemetryPort.TelemetrySnapshot snapshot() {
        return telemetry().snapshot();
    }

    @GetMapping("/alarms")
    public java.util.List<?> alarms() {
        return telemetry().alarmEngine().active();
    }

    @PostMapping("/alarms/{id}/clear")
    public Map<String, String> clearAlarm(@PathVariable String id) {
        telemetry().alarmEngine().clear(id);
        return Map.of("status", "ok");
    }

    @GetMapping(value = "/metrics", produces = "text/plain")
    public String metrics() {
        return telemetry().scrape();
    }

    @PostMapping("/config")
    public Map<String, String> config(@RequestBody Map<String, Object> body) {
        if (body.containsKey("autoReconfig")) {
            telemetry().setAutoReconfigEnabled(Boolean.TRUE.equals(body.get("autoReconfig")));
        }
        return Map.of("status", "ok");
    }
}
