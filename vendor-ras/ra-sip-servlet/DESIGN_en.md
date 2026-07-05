# SIP RA Design — JAIN SLEE 1.1 3-Port Contract

## JAIN SLEE 1.1 Layering

```
┌──────────────────────────────────────────────────────────────┐
│ SBB APPLICATION (Service Logic layer)                        │
│ • ProxySbb          — route SIP requests                     │
│ • RegistrationSbb   — handle REGISTER, bind AoR→Contact      │
│ • IceNegotiationSbb — receive IceCandidateEvent, select pair │
│ • DOES NOT KNOW IP/DNS — "blind" to network infrastructure   │
├──────────────────────────────────────────────────────────────┤
│ SIP RA (Resource Adaptor layer)                               │
│ ┌──────────────┬──────────────┬────────────────────────────┐ │
│ │ DnsResolver  │ StunClient   │ SIP Transport (Netty)      │ │
│ │ RFC 3263     │ ICE Candidate│ UDP/TCP/TLS/SCTP listener  │ │
│ │ NAPTR→SRV→A  │ gathering    │ SIP parser → typed events  │ │
│ │ Priority/Wt  │ keep-alive   │ Dialog tracking            │ │
│ └──────────────┴──────────────┴────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

## Packages

```
ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/
├── SipServletRaEndpoint.java         ← RaEndpointPort + RaCommandPort
├── SipServletResourceAdaptor.java    ← Orchestrator (transport + lifecycle)
├── SipRaConfig.java                  ← Config (DNS, STUN, ICE, ports, TLS)
├── dns/
│   ├── DnsResolver.java              ← RFC 3263 lookup (async CompletableFuture)
│   └── DnsResult.java                ← List<SipServer> sorted by priority/weight
├── stun/
│   ├── StunClient.java               ← STUN binding request (RFC 5389)
│   ├── StunResult.java               ← XOR-MAPPED-ADDRESS + keep-alive
│   └── IceCandidateCollector.java    ← Gather host/srflx/relay → fire events
├── transport/                        ← Netty adapters (9 files)
│   ├── SipTransport.java             ← Transport interface
│   ├── SipMessageSink.java           ← Inbound sink: bytes + peer + transport name
│   ├── AbstractNettyTransport.java   ← Shared Netty bootstrap logic
│   ├── UdpTransport.java             ← UDP listener (DatagramChannel)
│   ├── TcpTransport.java             ← TCP listener (ServerSocketChannel)
│   ├── TlsTransport.java             ← TLS listener (SIPS port)
│   ├── SctpTransport.java            ← SCTP listener stub
│   ├── SipMessageHandler.java        ← Netty handler → raw bytes → sink
│   └── SipTcpFrameDecoder.java       ← TCP frame decoder (Content-Length)
├── event/                            ← Typed SIP events (19 files)
│   ├── SipEvent.java                 ← sealed interface (19 permits)
│   ├── SipInviteEvent.java           ← + SDP body, Contact, Via, Route
│   ├── SipByeEvent.java
│   ├── SipAckEvent.java
│   ├── SipCancelEvent.java
│   ├── SipRegisterEvent.java         ← + Contact header, expires
│   ├── SipOptionsEvent.java
│   ├── SipResponseEvent.java         ← + status code, SDP, Via
│   ├── SipSubscribeEvent.java        ← RFC 6665 — event package, expires
│   ├── SipNotifyEvent.java           ← RFC 6665 — subscription state, body
│   ├── SipReferEvent.java            ← RFC 3515 — Refer-To header
│   ├── SipMessageEvent.java          ← RFC 3428 — SMS-over-IP
│   ├── SipInfoEvent.java             ← RFC 6086 — mid-dialog signaling
│   ├── SipUpdateEvent.java           ← RFC 3311 — pre-answer SDP update
│   ├── SipPrackEvent.java            ← RFC 3262 — provisional ACK
│   ├── SipPublishEvent.java          ← RFC 3903 — presence publication
│   ├── IceCandidateEvent.java        ← host/srflx/relay candidates
│   ├── IceCompletedEvent.java        ← selected pair
│   └── IceFailedEvent.java           ← failure reason
├── command/                          ← Sealed command hierarchy (10 files)
│   ├── SipOutboundCommand.java       ← sealed interface
│   ├── SendInvite.java               ← SBB gives AoR, RA resolves DNS
│   ├── SendBye.java
│   ├── SendAck.java
│   ├── SendCancel.java
│   ├── SendResponse.java
│   ├── StartIce.java                 ← SBB requests ICE gathering
│   ├── SelectIceCandidate.java       ← SBB selects optimal pair
│   ├── SendSdpUpdate.java            ← SBB sends updated SDP
│   └── SendMediaKeepAlive.java       ← SBB requests media keep-alive
└── collab/                           ← Collaborators (5 files)
    ├── SipEventClassifier.java       ← classify SIPMessage → typed SipEvent
    ├── DefaultSipEventClassifier.java ← JAIN-SIP API header extraction
    ├── SipOutboundSender.java        ← send command → wire
    ├── NettySipOutboundSender.java   ← Default: Netty + DNS resolution
    └── DialogRegistry.java           ← Per-peer channel registry + idle sweep
```

### Test suites (4 files, 13 tests)

```
ra-sip-servlet/src/test/java/com/microjainslee/ra/sipservlet/
├── SipEndToEndTest.java              ← Real socket E2E (UDP send/receive)
├── SipRaDialogLifecycleTest.java     ← Lifecycle: create → fire → endActivity
├── transport/SipTcpFrameDecoderTest.java ← TCP frame fragmentation/reassembly
└── collab/NettySipOutboundSenderTest.java ← DNS resolve → Netty write
```

## Data Flow

### Inbound (Network → RA → SBB)

```
UDP/TCP packet → Netty → SipMessageHandler → raw bytes
  → SipMessageSink.accept(bytes, peer, transport)
  → StringMsgParser (NIST/JAIN-SIP) → SIPMessage
  → DialogRegistry.recordInbound(sessionId, peer, transport)
  → SipEventClassifier.classify() → typed SipEvent
  → bootstrapPort.fireEvent(event, handle, null)
  → EventRouter (LMAX Disruptor) → VirtualThread → SBB.onEvent()
```

### Outbound (SBB → RA → Network)

```
SBB: raCommandPort.sendCommand(new SendInvite("alice@example.com", sdp))
  → SipServletRaEndpoint.sendCommand()
  → SipServletResourceAdaptor.sendOutbound()
  → NettySipOutboundSender.send(cmd)
    → DialogRegistry.find(sessionId) → peer address + transport
    → DnsResolver.resolve("example.com") → List<SipServer> sorted
    → Build SIP INVITE packet
    → Transport.send(bytes, target)
  → Netty → UDP/TCP packet → Network
```

### ICE Flow

```
1. SBB: sendCommand(new StartIce(callId))
2. RA: StunClient.sendBindingRequest() → receive XOR-MAPPED-ADDRESS
3. RA: IceCandidateCollector.gatherAll() → host + srflx candidates
4. RA: fireEvent(new IceCandidateEvent(callId, List<Candidate>))
5. SBB: receive event, select optimal pair
6. SBB: sendCommand(new SelectIceCandidate(callId, chosenPair))
7. SBB: sendCommand(new SendInvite(target, sdpWithCandidates))
   → RA: DnsResolver → Netty → SIP INVITE with SDP
8. Remote responds 200 OK with SDP
9. RA: fireEvent(new SipResponseEvent(200, remoteSdp))
10. SBB: extract candidates, verify connection
11. SBB: sendCommand(new SendAck(callId))
```

## 3-Port Contract

```java
// Port 1 — Lifecycle (container manages)
public interface RaEndpointPort {
    void activate(RaBootstrapPort bootstrap);
    void deactivate();
    String getRaName();              // e.g. "sip-servlet-ra"
}

// Port 2 — SBB → RA (commands)
public interface RaCommandPort {
    void sendCommand(OutboundCommand command);
}

// Port 3 — RA → SLEE (provided by container)
public interface RaBootstrapPort {
    ActivityHandle createActivityHandle(String id);
    void fireEvent(SleeEvent event, ActivityHandle handle, Address address);
    default void endActivity(ActivityHandle handle) {}
}
```

RA endpoint implements both Port 1 + Port 2:

```java
public final class SipServletRaEndpoint implements RaEndpointPort, RaCommandPort {
    private final SipServletResourceAdaptor delegate;

    @Override public String getRaName() { return "sip-servlet-ra"; }

    @Override public void activate(RaBootstrapPort bp) {
        delegate.setBootstrapPort(bp);
        delegate.raConfigure();
        delegate.raActive();               // opens Netty UDP/TCP/TLS
    }

    @Override public void deactivate() {
        delegate.raInactive();
        delegate.raUnconfigure();
    }

    @Override public void sendCommand(OutboundCommand cmd) {
        if (cmd instanceof SipOutboundCommand c) {
            delegate.sendOutbound(c);
        }
    }
}
```

Bootstrap wiring:

```java
SipServletResourceAdaptor ra = new SipServletResourceAdaptor();
SipServletRaEndpoint endpoint = new SipServletRaEndpoint(ra);
endpoint.setConfig(config);
container.registerRa(endpoint, endpoint);
// Container calls endpoint.activate(bootstrapPort)
// @InjectRa(name="sip-servlet-ra") resolves to endpoint
```

## DNS Resolution (RFC 3263)

SBB sends `SendInvite(targetAoR)` — only needs SIP URI, NO IP required.
RA automatically:
1. NAPTR lookup: `example.com` → `_sip._udp.example.com`
2. SRV lookup: `_sip._udp.example.com` → [`sip1.example.com:5060` pri=10 wt=50, `sip2.example.com:5060` pri=20 wt=50]
3. A/AAAA lookup: `sip1.example.com` → `10.0.0.1`
4. Sort by priority, select by weight (RFC 2782)
5. Try servers in order (failover)

```java
// DnsResolver.resolve() returns CompletableFuture<List<SipServer>>
public record DnsResult(List<DnsResolver.SipServer> servers) {
    public boolean isEmpty() { return servers.isEmpty(); }
    public DnsResolver.SipServer primary() { ... }
}
public record SipServer(String host, int port, int priority, int weight) {}
```

## STUN/ICE (RFC 5389 / RFC 8445)

- **StunClient**: sends STUN Binding Request to configurable STUN server
  - Parses XOR-MAPPED-ADDRESS from response
  - `StunResult(publicAddress, publicPort)` with `isValid()` method
  - Automatic keep-alive every 30s (configurable via SipRaConfig)
- **IceCandidateCollector**: enumerates network interfaces for host candidates
  - Combines with srflx candidates from STUN result
  - Calls `bootstrapPort.fireEvent(IceCandidateEvent)` directly
  - Candidate types: `host` (local IP), `srflx` (STUN), `relay` (TURN — stub)
  - Priority: RFC 5245 type-preference (126 host, 100 srflx)
- **SBB handles**: prioritization, negotiation, and pair selection logic

## Dialog/Session Leak Prevention

Three mandatory rules from [ra-guide.md](../../docs/ra-guide.md):

1. **Natural removal path**: protocol-ending messages (BYE, CANCEL, timeout)
   trigger `endSession()` → `bootstrapPort.endActivity(handle)`
2. **Idle sweeper**: `DialogRegistry.expireIdle(idleSecs)` runs on daemon
   `ScheduledExecutorService`, purges dialogs with no activity for N seconds
3. **raInactive() clears all**: transports stopped, `sessions` map cleared,
   all `endActivity()` called

## IMS / 4G / 5G Support

- All 16 SIP methods covered (INVITE through PUBLISH) — full RFC 3261 compliance
  plus extensions for VoLTE (4G) and VoNR (5G)
- `SipInviteEvent` includes Via, Route, Record-Route headers for proxy chain support
- `SipRegisterEvent` includes Contact + expires for IMS-AKA via 401/407 challenge
- IMS-specific headers (P-Access-Network-Info, P-Asserted-Identity,
  P-Charging-Vector) extractable via SipInviteEvent fields
- `SipSubscribeEvent` / `SipNotifyEvent` support reg-event package (RFC 3680)
  for third-party registration in IMS
- Diameter interworking: companion `ra-diameter` handles Cx/Sh/Gx/Ro interfaces

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| Sealed event hierarchy (19 permits) | Exhaustive `switch` pattern matching in SBB — compiler catches missing cases |
| Sealed command hierarchy (10 permits) | RA only accepts known commands; type-checked at compile time |
| JAIN-SIP API for header extraction | Only `StringMsgParser` (byte→Message) is NIST-level; all header access uses standard `javax.sip.*` |
| Per-peer channel registry (DialogRegistry) | TCP/TLS requires replying on the same connection (RFC 3261 §18.2.2) |
| Default NettySipOutboundSender | RA works out-of-box without app wiring; never silently drops commands |
| MessageSink carries peer address | UDP replies need source address; design lesson from initial implementation |
| Transport behind interface (SipTransport) | Enables future DPDK datapath swap without touching RA core |
| SipTcpFrameDecoder for TCP | Netty delivers arbitrary chunks; Content-Length framing required for message boundaries |
| corsac-sip dependency (NIST re-package) | Standard JAIN-SIP 1.2 RI, widely deployed in telecom; corsac-sip:sip-ri IS NIST SIP |

## File Count Summary

| Layer | Files |
|-------|------:|
| Core (endpoint + orchestrator + config) | 3 |
| dns/ | 2 |
| stun/ | 3 |
| transport/ | 9 |
| event/ | 19 |
| command/ | 10 |
| collab/ | 5 |
| **Total source** | **51** |
| Tests | 4 |
| **Grand total** | **55** |
