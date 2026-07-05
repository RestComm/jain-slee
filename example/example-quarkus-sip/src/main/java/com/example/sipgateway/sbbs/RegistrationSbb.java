package com.example.sipgateway.sbbs;

import com.microjainslee.api.ActivityContextInterface;
import com.microjainslee.api.RaCommandPort;
import com.microjainslee.api.Sbb;
import com.microjainslee.api.SleeEvent;
import com.microjainslee.api.SleeEventHandler;
import com.microjainslee.api.annotations.InjectRa;
import com.microjainslee.ra.sipservlet.command.SendResponse;
import com.microjainslee.ra.sipservlet.event.SipRegisterEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SIP Registrar SBB — RFC 3261 REGISTER handler.
 * <p>Stores AoR → Contact bindings in-memory. Sends 200 OK responses
 * via {@code @InjectRa}. Fires {@link RegistrationUpdatedEvent}
 * for downstream consumers (e.g., presence, push notifications).
 */
public class RegistrationSbb implements Sbb, SleeEventHandler {

    private static final Logger LOG = LogManager.getLogger(RegistrationSbb.class);
    private final ConcurrentMap<String, List<String>> registrations = new ConcurrentHashMap<>();

    @InjectRa(name = "sip-servlet-ra")
    private volatile RaCommandPort sipRa;

    public RegistrationSbb() { /* no-arg required for entity pool */ }
    @Override public void sbbRemove() { }

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof SipRegisterEvent reg) {
            onSipRegisterEvent(reg, aci);
        }
    }

    public void onSipRegisterEvent(SipRegisterEvent event, ActivityContextInterface aci) {
        String aor = event.toUri();
        String contact = event.contactUri();
        int expires = event.expires();
        LOG.info("[RegistrationSbb] REGISTER aor={} contact={} expires={} callId={}",
                aor, contact, expires, event.callId());

        if (expires == 0) {
            registrations.computeIfPresent(aor, (k, contacts) -> {
                contacts.remove(contact);
                return contacts.isEmpty() ? null : contacts;
            });
            LOG.info("[RegistrationSbb] Unregistered aor={} contact={}", aor, contact);
        } else {
            registrations.merge(aor,
                    new ArrayList<>(List.of(contact)),
                    (old, nu) -> {
                        if (!old.contains(contact)) old.add(contact);
                        return old;
                    });
            LOG.info("[RegistrationSbb] Registered aor={} contact={} expires={}s", aor, contact, expires);
        }

        // Fires custom event to downstream SBBs (e.g., presence, push notifications)
        LOG.debug("[RegistrationSbb] RegistrationUpdated: aor={} contact={} expires={}s", aor, contact, expires);

        RaCommandPort port = this.sipRa;
        if (port != null) {
            port.sendCommand(new SendResponse(event.callId(), 200, "OK"));
        } else {
            LOG.warn("[RegistrationSbb] sipRa not injected - cannot send 200 OK");
        }
    }

    public List<String> lookup(String aor) {
        return registrations.getOrDefault(aor, List.of());
    }

    public ConcurrentMap<String, List<String>> getRegistrations() {
        return registrations;
    }
}
