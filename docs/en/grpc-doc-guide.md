# Trace: gRPC Unary Call Event Flow — Client → RA → SBB → Response

> Full end-to-end trace of a stub-less gRPC unary call entering the
> generic `ra-grpc-server`, routing to an SBB, and the response bytes
> going back to the wire. Companion trace for the outbound direction via
> `ra-grpc-client` at the end.
>
> Both RAs are **bytes-level generic**: no generated stubs anywhere.
> Protobuf encode/decode is the application's job (inside the SBB or a
> thin mapper). The RAs are pure transport — same rule as every
> micro-jainslee RA.

---

## Files involved (inbound, ra-grpc-server)

| Step | File | Role |
|------|------|------|
| 1 | `GrpcServerRa.java` `raActive()` | NettyServerBuilder + fallback `HandlerRegistry` |
| 2 | `GrpcServerRa.java` `genericMethod()` | Materialise `MethodDescriptor<byte[],byte[]>` for ANY method name |
| 3 | `collab/BytesMarshaller.java` | Identity marshaller — raw message bytes through |
| 4 | `GrpcServerRa.java` `startUnaryCall()` | callId + metadata + `collab/PendingCallRegistry` |
| 5 | `events/GrpcRequestEvent.java` | The typed event SBBs receive |
| 6 | `jainslee-core` `MicroSleeContainer.routeEvent` | `mapEventToSbb(GrpcRequestEvent, "EchoSbb")` + IES |
| 7 | your SBB `onEvent()` | decode → business logic → `sendCommand(...)` |
| 8 | `command/SendGrpcResponse.java` / `SendGrpcError.java` | SBB → RA commands |
| 9 | `GrpcServerRaEndpoint.java` `sendCommand()` | Route command → `GrpcServerRa.sendOutbound()` |
| 10 | `GrpcServerRa.java` `completeCall()` | `ServerCall.sendMessage(bytes)` + `close(Status.OK)` |

Module structure — identical template to every vendor RA:

```
ra-grpc-server/
├── GrpcServerRa.java          # core transport
├── GrpcServerRaEndpoint.java  # 3-port wrapper
├── events/    GrpcRequestEvent
├── command/   GrpcServerCommand (sealed) → SendGrpcResponse | SendGrpcError
└── collab/    BytesMarshaller, PendingCallRegistry
```

---

## Full trace (10 steps)

```
     NETWORK                             RA                              CORE                SBB
==================================================================================================

1 HTTP/2 frame on :9090
   POST /billing.ChargingService/Charge
   content-type: application/grpc
   x-session-id: sess-42            (metadata)
   <5-byte gRPC frame><protobuf request bytes>
   |
   v
2 grpc-netty-shaded ServerImpl
   -> method lookup fails in the (empty) primary registry
   -> fallbackHandlerRegistry.lookupMethod("billing.ChargingService/Charge")
   -> GrpcServerRa.genericMethod(): builds
        MethodDescriptor<byte[],byte[]>(UNARY, "billing.ChargingService/Charge",
                                        BytesMarshaller, BytesMarshaller)
   |  ← this is why ANY service/method works with zero registration
   v
3 GrpcServerRa.startUnaryCall(method, call, headers)
   +-- callId = UUID                       e.g. "1c3e..."
   +-- metadata = {x-session-id: sess-42, user-agent: ...}
   +-- activityId = metadata[correlationMetadataKey]   (if configured)
   |               else callId              → one activity per call
   +-- pendingCalls.register(callId, PendingCall(call, activityId, deadline))
   +-- call.request(2)                      (flow control credit)
   |
   v  onMessage(byte[]) → onHalfClose()
4 GrpcServerRa.fireRequest()
   +-- endpoint().startActivity(activityId)   → container creates the ACI
   +-- endpoint().fireEvent(activityId,
          GrpcRequestEvent(callId, "billing.ChargingService/Charge",
                           requestBytes, metadata, activityId))
   |
   v
5 MicroSleeContainer.routeEvent(event, aci)
   +-- eventToSbbMap: GrpcRequestEvent → "ChargeSbb"
   +-- ACI empty → IES dispatcher (or Type/aciName) → acquireEntity
   |     → @InjectRa("grpc-server-ra") RaCommandPort injected
   +-- attach + EventRouter (LMAX) → entity virtual thread
   |
   v
6 ChargeSbb.onEvent(GrpcRequestEvent request, aci)
   +-- ChargeRequest req = ChargeRequest.parseFrom(request.payload());  // APP decodes
   +-- ... business logic ...
   +-- byte[] resp = ChargeResponse.newBuilder()...build().toByteArray();
   +-- grpcRa.sendCommand(new SendGrpcResponse(request.callId(), resp));
   |     (error path: new SendGrpcError(callId, 5, "not found"))
   |
   v
7 GrpcServerRaEndpoint.sendCommand(cmd)
   -> cmd instanceof GrpcServerCommand → delegate.sendOutbound(cmd)
   |
   v
8 GrpcServerRa.completeCall(callId)
   +-- pending = pendingCalls.remove(callId)
   +-- pending.call().sendHeaders(new Metadata())
   +-- pending.call().sendMessage(respBytes)
   +-- pending.call().close(Status.OK, trailers)
   +-- endCallActivity(activityId)      → ActivityEndedEvent → ACI reclaimed
   |                                      (per-call mode only; correlated
   |                                       sessions end via the app)
   v
9 grpc-netty-shaded writes HTTP/2 response frames
   <5-byte frame><protobuf response bytes> + trailers grpc-status: 0
   |
   v
10 CLIENT receives the unary response
```

**Timeout guard:** if no SBB answers within `callTimeoutMillis`
(default 30 s), the sweeper closes the call with `DEADLINE_EXCEEDED` and
frees the pending entry — a stuck SBB can never leak open calls.

---

## Outbound direction (ra-grpc-client)

```
SBB                          RA (ra-grpc-client)                       REMOTE SERVER
====================================================================================

RequesterSbb
  grpcClient.sendCommand(
    new InvokeGrpc("corr-1",              // command/InvokeGrpc.java
        "billing-host:9090",
        "billing.ChargingService/Charge",
        requestBytes))
        |
        v
GenericGrpcClientRaEndpoint.sendCommand → GenericGrpcClientRa.sendOutbound
        |
        +-- virtual-thread pool → doInvoke()      (SBB entity never blocks)
        +-- channels.computeIfAbsent("billing-host:9090")   (pooled ManagedChannel)
        +-- MethodDescriptor<byte[],byte[]> built at call time (no stubs)
        +-- ClientCalls.blockingUnaryCall(channel, method, deadline, bytes)
        |
        |   ══════════ HTTP/2 to billing-host:9090 ══════════►
        |   ◄═════════ response bytes / grpc-status ══════════
        v
fireResponse:
  events/GrpcInvokeResponseEvent(corrId, target, method,
                                 payloadBytes, statusCode, statusDesc)
  fired on activity "corr-1"
        |
        v
MicroSleeContainer.routeEvent → mapEventToSbb(GrpcInvokeResponseEvent, "RequesterSbb")
        |
        v
RequesterSbb.onEvent(GrpcInvokeResponseEvent) → response.isOk() ? decode : handle status
```

---

## Key data structures

| Structure | Where | Purpose |
|---|---|---|
| `PendingCallRegistry` | ra-grpc-server/collab | callId → open ServerCall + deadline; drained on shutdown |
| `BytesMarshaller` | ra-grpc-server/collab | identity marshaller → schema-agnostic RA |
| channel pool (`Map<String,ManagedChannel>`) | ra-grpc-client | one HTTP/2 connection per target, reused |
| `correlationMetadataKey` | GrpcServerRa | metadata value → SLEE activity id (stateful sessions) |

## Key interfaces

| Interface | Direction | Contract |
|---|---|---|
| `RaEndpointPort` | container → RA | activate(bootstrap) / deactivate / getRaName |
| `RaCommandPort` | SBB → RA | `sendCommand(SendGrpcResponse \| SendGrpcError \| InvokeGrpc)` |
| `RaBootstrapPort` | RA → SLEE | createActivityHandle / fireEvent / endActivity |

## Summary: 4 layers, 2 directions

Same shape as the SIP trace (`sip-servlet-doc-guide.md`): network ⇄
transport (grpc-netty) ⇄ RA (bytes ⇄ events/commands) ⇄ core routing ⇄
SBB. Swap "SIP dialog / Call-ID" for "gRPC call / metadata correlation"
and the model is identical — that is the point of the shared RA template.

Verified end-to-end by `GrpcServerEndToEndTest` (real socket, arbitrary
method names, error status) and `GenericGrpcClientEndToEndTest` (both
generic RAs in one container, full loop with zero generated code).
