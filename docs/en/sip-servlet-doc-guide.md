# Trace: SIP REGISTER Event Flow — SBB → RA → SIP → Network

> Full end-to-end trace of a SIP REGISTER request entering the RA, routing to RegistrationSbb, and the 200 OK response going back to the wire.

---

## Files involved

| Step | File | Role |
|------|------|------|
| 1 | `transport/SipMessageHandler.java` | Netty handler → raw bytes |
| 2 | `SipServletResourceAdaptor.java` `onRawMessage()` | Parse + classify + fireEvent |
| 3 | `collab/DefaultSipEventClassifier.java` | SIP method → typed `SipRegisterEvent` |
| 4 | `SipServletResourceAdaptor.java` `fireEvent()` | `bootstrapPort.fireEvent(event, handle)` |
| 5 | `jainslee-core` `EventRouter` | Lookup `mapEventToSbb` → `RegistrationSbb` |
| 6 | `sbbs/RegistrationSbb.java` `onEvent()` | Business logic → `sendCommand(SendResponse)` |
| 7 | `SipServletRaEndpoint.java` `sendCommand()` | Route command → delegate |
| 8 | `collab/NettySipOutboundSender.java` | Encode `SendResponse` → `SIPResponse` → transmit |

---

## Full trace (8 steps)

```
     NETWORK                              RA                            CORE                  SBB
===================================================================================================

1 UDP packet:5060
   \"REGISTER sip:ims.example.org SIP/2.0
    Via: SIP/2.0/UDP 192.168.1.5:5060;branch=z9hG4bK-abc
    From: <sip:alice@ims.home>;tag=xyz
    To: <sip:alice@ims.home>
    Call-ID: abc123@192.168.1.5
    CSeq: 1 REGISTER
    Contact: <sip:alice@192.168.1.5:5060>
    Expires: 3600
    Content-Length: 0\"

   |
   +-- Netty NioDatagramChannel -> SipMessageHandler.channelRead0()
   |   -> DatagramPacket.content() -> byte[402]
   |   -> messageSink.accept(bytes, peer, \"UDP\")
   |
   v
2 SipServletResourceAdaptor.onRawMessage(bytes, peer, \"UDP\")
   |
   +-- StringMsgParser (NIST) -> SIPMessage (gov.nist.javax.sip.message.SIPRequest)
   |
   +-- deriveCallId(msg) -> \"abc123@192.168.1.5\"
   |   via javax.sip.header.CallIdHeader -> getCallId()
   |
   +-- dialogs.computeIfAbsent(\"abc123@...\",
   |       id -> bootstrapPort.createActivityHandle(id))
   |   -> ActivityHandle[cid=abc123@...]
   |
   +-- dialogRegistry.recordInbound(\"abc123@...\", handle, peer, \"UDP\")
   |   -> Dialog(callId, peer=\"192.168.1.5:5060\", transport=\"UDP\", lastRequest)
   |
   v
3 DefaultSipEventClassifier.classify(msg, \"abc123@192.168.1.5\")
   |
   +-- msg instanceof Request req
   |
   +-- req.getMethod() -> \"REGISTER\"
   |
   +-- switch(\"REGISTER\") {
   |       case \"REGISTER\" -> new SipRegisterEvent(
   |           callId     = \"abc123@192.168.1.5\",
   |           fromUri    = \"From: <sip:alice@ims.home>;tag=xyz\",
   |           toUri      = \"To: <sip:alice@ims.home>\",
   |           contactUri = \"Contact: <sip:alice@192.168.1.5:5060>\",
   |           expires    = 3600
   |       )
   |   }
   |
   v
4 bootstrapPort.fireEvent(
       SipRegisterEvent(\"abc123@...\", \"From: ...\", \"To: ...\", \"Contact: ...\", 3600),
       ActivityHandle[cid=abc123@...],
       null
   )
   |
   +-- RaBootstrapPort.fireEvent() -> EventRouter (LMAX Disruptor RingBuffer)
   |
   v
5 EventRouter.routeEvent()
   |
   +-- event.getClass() -> SipRegisterEvent.class
   +-- lookup initialEventIndex[SipRegisterEvent.class]
   |   -> [\"RegistrationSbb\"]  (from mapEventToSbb)
   |
   +-- entityPool.acquire(\"RegistrationSbb#42\", RegistrationSbb::new)
   |   -> new RegistrationSbb()
   |   -> @InjectRa(name=\"sip-servlet-ra\") -> sipRa = SipServletRaEndpoint
   |   -> park VirtualThread #27 -> sbbCreate() -> sbbActivate()
   |
   v
6 RegistrationSbb.onEvent(SipRegisterEvent event, ActivityContextInterface aci)
   |                              REGISTER
   |   event.callId()      -> \"abc123@192.168.1.5\"
   |   event.fromUri()     -> \"From: <sip:alice@ims.home>;tag=xyz\"
   |   event.toUri()       -> \"To: <sip:alice@ims.home>\"
   |   event.contactUri()  -> \"Contact: <sip:alice@192.168.1.5:5060>\"
   |   event.expires()     -> 3600
   |
   +-- BUSINESS LOGIC:
   |   if (expires == 0)     -> unregister (remove contact)
   |   else                   -> register (add to ConcurrentMap)
   |   registrations.merge(aor, [contact], ...)
   |
   v
   sipRa.sendCommand(
       new SendResponse(
           callId     = \"abc123@192.168.1.5\",
           statusCode = 200,
           reason     = \"OK\"
       )
   )
   |
   +-- sipRa is SipServletRaEndpoint (same object as endpoint)
   |
   v
7 SipServletRaEndpoint.sendCommand(OutboundCommand command)
   |
   +-- command instanceof SipOutboundCommand sipCmd
   |   -> delegate.sendOutbound(sipCmd)
   |
   v
8 NettySipOutboundSender.send(SipOutboundCommand cmd)
   |
   +-- switch(cmd) {
   |       case SendResponse c ->
   |           sendResponse(\"abc123@...\", 200, \"OK\", null)
   |   }
   |
   +-- dialogs.find(\"abc123@192.168.1.5\")
   |   -> Dialog {
   |       callId      = \"abc123@192.168.1.5\",
   |       peer        = 192.168.1.5:5060,
   |       transport   = \"UDP\",
   |       lastRequest = SIPRequest(REGISTER, ...)
   |   }
   |
   +-- Build SIP 200 OK using NIST SIP:
   |   SIPResponse response = request.createResponse(200, \"OK\")
   |   -> SIP/2.0 200 OK
   |        Via: SIP/2.0/UDP 192.168.1.5:5060;branch=z9hG4bK-abc
   |        From: <sip:alice@ims.home>;tag=xyz
   |        To: <sip:alice@ims.home>;tag=def456        <- localTag
   |        Call-ID: abc123@192.168.1.5
   |        CSeq: 1 REGISTER
   |        Contact: <sip:server@10.0.0.1:5060>         <- localContact
   |        Content-Length: 0
   |
   +-- transmit(response, \"UDP\", 192.168.1.5:5060)
   |   -> transport.send(response.toString().getBytes(),
   |                        new InetSocketAddress(\"192.168.1.5\", 5060))
   |   -> UdpTransport: DatagramChannel.write(packet, 192.168.1.5:5060)
   |
   v
   NETWORK
   UDP:5060 -> 192.168.1.5:5060
   \"SIP/2.0 200 OK
    Via: SIP/2.0/UDP 192.168.1.5:5060;branch=z9hG4bK-abc
    From: <sip:alice@ims.home>;tag=xyz
    To: <sip:alice@ims.home>;tag=def456
    Call-ID: abc123@192.168.1.5
    CSeq: 1 REGISTER
    Contact: <sip:server@10.0.0.1:5060>
    Content-Length: 0\"
```

---

## Code trace by file

### Step 2: SipServletResourceAdaptor.onRawMessage()

Called by `SipMessageSink.onMessage(bytes, peer, transport)` when Netty receives a UDP packet:

```java
void onRawMessage(byte[] raw, InetSocketAddress peer, String transportName) {
    // 1. Parse raw SIP bytes using NIST JAIN-SIP parser
    StringMsgParser parser = new StringMsgParser();
    SIPMessage sipMsg = parser.parseSIPMessage(raw, true, false, null);
    // -> SIPRequest with method="REGISTER"

    // 2. Extract Call-ID via javax.sip.* API
    String callId = deriveCallId(sipMsg);
    // -> "abc123@192.168.1.5"

    // 3. Create or reuse ActivityHandle for this dialog
    ActivityHandle handle = dialogs.computeIfAbsent(callId,
            id -> bootstrapPort.createActivityHandle(id));

    // 4. Record dialog state: remember peer address + transport for reply
    dialogRegistry.recordInbound(callId, handle, peer, transportName, sipMsg);

    // 5. Classify raw SIPMessage -> typed SipEvent
    SipEvent event = classifier.classify(sipMsg, callId);

    // 6. Fire event into SLEE EventRouter
    if (event != null) {
        bootstrapPort.fireEvent(event, handle, null);
    }
}
```

### Step 3: DefaultSipEventClassifier.classify()

```java
private SipEvent classifyRequest(Request req, String callId) {
    return switch (req.getMethod().toUpperCase()) {
        case "INVITE"   -> new SipInviteEvent(callId, from, to, contact, via, sdp, ...);
        case "BYE"      -> new SipByeEvent(callId);
        case "ACK"      -> new SipAckEvent(callId);
        case "CANCEL"   -> new SipCancelEvent(callId);
        case "REGISTER" -> new SipRegisterEvent(
            callId,
            extractFrom(req),      // javax.sip.header.FromHeader -> toString()
            extractTo(req),        // javax.sip.header.ToHeader -> toString()
            extractContact(req),   // javax.sip.header.ContactHeader -> toString()
            extractExpires(req)    // javax.sip.header.ExpiresHeader -> getExpires()
        );
        case "OPTIONS"  -> new SipOptionsEvent(callId);
        // ... 10 more methods ...
        default -> null;  // silently drop unknown methods
    };
}
```

### Step 5: Bootstrap wiring - mapEventToSbb

```java
// SipGatewayBootstrap.mapEventToSbb() - called during @PostConstruct
container.registerSbbType(RegistrationSbb.class, RegistrationSbb::new);
container.createIesDispatcher();
container.mapEventToSbb(SipRegisterEvent.class, "RegistrationSbb");

// Internally, container builds:
//   initialEventIndex["SipRegisterEvent"] = ["RegistrationSbb"]
//
// When SipRegisterEvent arrives at EventRouter:
//   1. Lookup initialEventIndex -> finds ["RegistrationSbb"]
//   2. Acquire RegistrationSbb entity from pool (or create new)
//   3. @InjectRa(name="sip-servlet-ra")
//      -> injects SipServletRaEndpoint into sipRa field
//   4. Park VirtualThread -> call sbbCreate(), sbbActivate()
//   5. Call RegistrationSbb.onEvent(event, aci)
```


### Step 6: RegistrationSbb.onEvent()

File: `example/example-quarkus-sip/src/main/java/com/example/sipgateway/sbbs/RegistrationSbb.java`

```java
public class RegistrationSbb implements Sbb, SleeEventHandler {

    @InjectRa(name = "sip-servlet-ra")   // injected by container on entity creation
    private volatile RaCommandPort sipRa; // same object as SipServletRaEndpoint

    private final ConcurrentMap<String, List<String>> registrations = new ConcurrentHashMap<>();

    @Override
    public void onEvent(SleeEvent event, ActivityContextInterface aci) {
        if (event instanceof SipRegisterEvent reg) {
            onSipRegisterEvent(reg, aci);
        }
    }

    void onSipRegisterEvent(SipRegisterEvent event, ActivityContextInterface aci) {
        String aor     = event.toUri();        // "sip:alice@ims.home"
        String contact = event.contactUri();    // "sip:alice@192.168.1.5:5060"
        int expires    = event.expires();        // 3600

        // === BUSINESS LOGIC: update registration table ===
        if (expires == 0) {
            // Unregister: remove contact for this AoR
            registrations.computeIfPresent(aor, (k, contacts) -> {
                contacts.remove(contact);
                return contacts.isEmpty() ? null : contacts;
            });
        } else {
            // Register: add contact to AoR binding
            registrations.merge(aor,
                new ArrayList<>(List.of(contact)),
                (old, nu) -> {
                    if (!old.contains(contact)) old.add(contact);
                    return old;
                });
        }

        // === SEND 200 OK BACK TO UA via RA ===
        RaCommandPort port = this.sipRa;
        if (port != null) {
            port.sendCommand(
                new SendResponse(
                    "abc123@192.168.1.5",  // callId
                    200,                     // statusCode
                    "OK"                     // reason
                )
            );
        }
    }
}
```

### Step 7: SipServletRaEndpoint.sendCommand()

```java
public final class SipServletRaEndpoint implements RaEndpointPort, RaCommandPort {
    private final SipServletResourceAdaptor delegate;

    @Override
    public void sendCommand(OutboundCommand command) {
        if (command instanceof SipOutboundCommand sipCmd) {
            delegate.sendOutbound(sipCmd);
            // forwards to SipServletResourceAdaptor
            // which calls outboundSender.send(sipCmd)
        } else {
            LOG.warn("SIP RA received unknown command: {}", command);
        }
    }
}
```

### Step 8: NettySipOutboundSender.send() -> transmit()

File: `vendor-ras/ra-sip-servlet/src/main/java/com/microjainslee/ra/sipservlet/collab/NettySipOutboundSender.java`

```java
@Override
public void send(SipOutboundCommand cmd) {
    switch (cmd) {
        case SendResponse c  -> sendResponse(c.callId(), c.statusCode(), c.reason(), null);
        case SendSdpUpdate c -> sendResponse(c.callId(), 200, "OK", c.sdp());
        case SendBye c       -> sendInDialogRequest(c.callId(), Request.BYE);
        case SendAck c       -> sendAck(c.callId());
        case SendCancel c    -> sendCancel(c.callId());
        case SendInvite c    -> sendInvite(c);
        default -> LOG.warn("unsupported command: {}", cmd);
    }
}

private void sendResponse(String callId, int status, String reason, String sdp) {
    // 1. Find dialog state from inbound message
    DialogRegistry.Dialog dialog = dialogs.find(callId);
    SIPRequest request = dialog.lastRequest();   // the REGISTER we received

    // 2. Build SIP response using NIST createResponse()
    SIPResponse response = request.createResponse(200, "OK");
    // Creates response with headers mirrored from request:
    //   Via, From, Call-ID, CSeq — all auto-copied

    // 3. Set To-tag for non-100 response
    if (status > 100) {
        ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
        if (to.getTag() == null) {
            to.setTag(localTag(callId));   // stable per-dialog tag
        }
    }

    // 4. Add Contact header for 2xx REGISTER/INVITE
    if (status/100 == 2 && needsContact(request.getMethod())) {
        response.setHeader(localContact(dialog.transport()));
    }

    // 5. Send raw bytes via transport
    transmit(response, dialog.transport(), dialog.peer());
    // response.toString() -> ~350 bytes SIP text
    // transport.send(bytes, InetSocketAddress("192.168.1.5", 5060))
    // UdpTransport: DatagramChannel.write(packet, "192.168.1.5:5060")
}
```

---

## Key data structures

| Record | Fields | Created by |
|--------|--------|------------|
| `SipRegisterEvent` | `(String callId, String fromUri, String toUri, String contactUri, int expires)` | `DefaultSipEventClassifier.classify()` |
| `SendResponse` | `(String callId, int statusCode, String reason)` | `RegistrationSbb.onSipRegisterEvent()` |
| `DialogRegistry.Dialog` | `(String callId, InetSocketAddress peer, String transport, SIPMessage lastRequest, SIPMessage lastResponse)` | `DialogRegistry.recordInbound()` |

## Key interfaces

| Interface | Method | Purpose |
|-----------|--------|---------|
| `RaEndpointPort` | `void activate(RaBootstrapPort)` | RA lifecycle -> open transport |
| `RaCommandPort` | `void sendCommand(OutboundCommand)` | SBB -> RA outbound |
| `RaBootstrapPort` | `void fireEvent(SleeEvent, ActivityHandle, Address)` | RA -> SLEE inbound |
| `SleeEventHandler` | `void onEvent(SleeEvent, ActivityContextInterface)` | SBB receives event |
| `SipEvent` | `sealed interface extends SleeEvent` | Typed SIP event hierarchy (19 permits) |
| `SipOutboundCommand` | `sealed interface extends OutboundCommand` | Typed SIP command hierarchy (10 permits) |

## Summary: 4 layers, 2 directions

```
DIRECTION: INBOUND (Network -> SBB)       DIRECTION: OUTBOUND (SBB -> Network)
=====================================    =====================================
Netty -> raw bytes                        SBB: sendCommand(SendResponse)
  -> StringMsgParser -> SIPMessage          -> RaCommandPort.sendCommand()
  -> deriveCallId                            -> instanceof SipOutboundCommand
  -> ActivityHandle (ACI)                    -> SipServletResourceAdaptor
  -> DialogRegistry.recordInbound            -> NettySipOutboundSender
  -> classify -> SipRegisterEvent             -> DialogRegistry.find(callId)
  -> fireEvent -> EventRouter                  -> request.createResponse(200)
  -> mapEventToSbb lookup                      -> set To-tag, Contact
  -> entityPool.acquire                        -> transport.send(bytes, peer)
  -> @InjectRa -> sipRa field              Netty -> UDP packet
  -> SBB.onEvent(event, aci)
```
