# SIP RA Design — JAIN SLEE 1.1 3-Port Contract

## Phân tầng JAIN SLEE 1.1

```
┌──────────────────────────────────────────────────────────────┐
│ SBB APPLICATION (tầng Service Logic)                         │
│ • ProxySbb          — route SIP requests                     │
│ • RegistrationSbb   — handle REGISTER, bind AoR→Contact      │
│ • IceNegotiationSbb — receive IceCandidateEvent, select pair │
│ • KHÔNG BIẾT IP/DNS — "mù" về hạ tầng mạng                  │
├──────────────────────────────────────────────────────────────┤
│ SIP RA (tầng Resource Adaptor)                               │
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
├── SipServletRaEndpoint.java       ← RaEndpointPort + RaCommandPort
├── SipServletResourceAdaptor.java  ← Orchestrator
├── SipRaConfig.java                ← Config (DNS, STUN, ICE, ports)
├── dns/
│   ├── DnsResolver.java            ← RFC 3263 lookup (async CompletableFuture)
│   └── DnsResult.java              ← List<SipServer> sorted by priority/weight
├── stun/
│   ├── StunClient.java             ← STUN binding request (RFC 5389)
│   ├── StunResult.java             ← XOR-MAPPED-ADDRESS + keep-alive
│   └── IceCandidateCollector.java  ← Gather host/srflx/relay → fire events
├── transport/
│   ├── SipTransport.java
│   ├── AbstractNettyTransport.java
│   ├── TcpTransport.java
│   ├── UdpTransport.java
│   ├── SctpTransport.java
│   ├── TlsTransport.java           ← NEW: TLS support
│   └── SipMessageHandler.java
├── event/
│   ├── SipEvent.java               ← sealed interface (unchanged)
│   ├── SipInviteEvent.java         ← + SDP body, Contact, Route headers
│   ├── SipByeEvent.java
│   ├── SipAckEvent.java
│   ├── SipCancelEvent.java
│   ├── SipRegisterEvent.java       ← + Contact header, expires
│   ├── SipOptionsEvent.java
│   ├── SipResponseEvent.java       ← + response headers
│   ├── IceCandidateEvent.java      ← NEW: IP, port, type(host/srflx/relay), priority
│   ├── IceCompletedEvent.java      ← NEW: selected pair
│   └── IceFailedEvent.java         ← NEW: failure reason
├── command/
│   ├── SipOutboundCommand.java     ← sealed interface (unchanged)
│   ├── SendInvite.java             ← + targetURI (SBB gives AoR, RA resolves DNS)
│   ├── SendBye.java
│   ├── SendResponse.java
│   ├── SendAck.java
│   ├── SendCancel.java
│   ├── StartIce.java               ← NEW: SBB requests ICE gathering
│   ├── SelectIceCandidate.java     ← NEW: SBB selects optimal pair
│   ├── SendSdpUpdate.java          ← NEW: SBB sends updated SDP
│   └── SendMediaKeepAlive.java     ← NEW: SBB requests media keep-alive
└── collab/
    ├── SipEventClassifier.java
    ├── SipOutboundSender.java       ← EXTENDED: now handles DNS resolution + sending
    └── DefaultSipEventClassifier.java
```

## Data Flow

### Inbound (Network → RA → SBB)
```
UDP packet → Netty → StringMsgParser → SIPMessage
  → DnsResolver.verify (optional)
  → SipEventClassifier.classify() → typed SipEvent
  → bootstrapPort.fireEvent(event, handle, address)
  → EventRouter → VirtualThread → SBB.onEvent()
```

### Outbound (SBB → RA → Network)
```
SBB: raCommandPort.sendCommand(new SendInvite("alice@example.com", sdp))
  → SipServletRaEndpoint.sendCommand()
  → SipServletResourceAdaptor.sendOutbound()
  → DnsResolver.resolve("example.com") → List<SipServer> sorted
  → SipOutboundSender.send(cmd, resolvedServers)
  → Netty → UDP packet → Network
```

### ICE Flow
```
1. SBB: sendCommand(new StartIce(callId))
2. RA: StunClient.sendBindingRequest() → receive XOR-MAPPED-ADDRESS
3. RA: IceCandidateCollector.gather() → host + srflx candidates
4. RA: fireEvent(new IceCandidateEvent(callId, List<Candidate>))
5. SBB: nhận event, select optimal pair
6. SBB: sendCommand(new SelectIceCandidate(callId, chosenPair))
7. SBB: sendCommand(new SendInvite(target, sdpWithCandidates))
   → RA: DnsResolver → Netty → SIP INVITE with SDP
8. Remote responds 200 OK with SDP
9. RA: fireEvent(new SipResponseEvent(200, remoteSdp))
10. SBB: extract candidates, verify connection
11. SBB: sendCommand(new SendAck(callId))
```

## DNS Resolution (RFC 3263)

SBB sends `SendInvite(targetAoR)` — chỉ cần SIP URI, KHÔNG cần IP.
RA tự động:
1. NAPTR lookup: `example.com` → `_sip._udp.example.com`
2. SRV lookup: `_sip._udp.example.com` → [`sip1.example.com:5060` pri=10 wt=50, `sip2.example.com:5060` pri=20 wt=50]
3. A/AAAA lookup: `sip1.example.com` → `10.0.0.1`
4. Sort by priority, select by weight
5. Try servers in order (failover)

## STUN/ICE (RFC 5389 / RFC 8445)

- `StunClient`: sends STUN Binding Request to STUN server (configurable)
- Parses XOR-MAPPED-ADDRESS from response
- Automatic keep-alive every 30s
- `IceCandidateCollector`: combines host candidates (local IPs) + srflx (STUN result)
- Fires `IceCandidateEvent(callId, candidates)` to SBB
- SBB handles prioritization and selection logic
