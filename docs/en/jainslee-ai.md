# jainslee-ai — AI Operations Agent

> Pure-Java AI operations agent for micro-jainslee: reads telemetry snapshots,
> asks an OpenAI-compatible LLM for analysis and recommendations, and acts
> through the autonomous guardian and telemetry control surface under strict
> guardrails.

**Module:** `com.microjainslee:jainslee-ai`
**Package:** `com.microjainslee.ai`
**Since:** 1.2.0

---

## Overview

The AI agent is an **optional** autonomous operations layer that sits above the
telemetry and autonomous modules. It is **disabled by default** and must be
explicitly enabled + configured with an API key.

```
┌─────────────────────────────────────────────────────────┐
│                   REST / GUI / CLI                       │
│   (enable/disable, mode switch, "Analyze now", reports)  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                   AIAgentEngine                          │
│   Virtual-thread loop, pre-filter, guard, execute        │
└──────┬──────────────┬─────────────────┬─────────────────┘
       │              │                 │
       ▼              ▼                 ▼
┌──────────┐  ┌──────────────┐  ┌──────────────────┐
│ AIAdvisor│  │ ActionGuard  │  │ AutonomousGuardian│
│ (LLM)    │  │ (allow-list) │  │ (relief actions)  │
└──────┬───┘  └──────────────┘  └──────────────────┘
       │
       ▼
┌──────────────────┐
│OpenAiCompatAdvisor│
│ /chat/completions │
└──────────────────┘

## Architecture

### Core Components

| Class | Role | Key Detail |
|---|---|---|
| `AIAgentEngine` | Control loop + runtime API | Virtual thread, AutoCloseable |
| `AIAdvisor` | Pluggable LLM backend interface | Thread-safe contract |
| `OpenAiCompatAdvisor` | Default: OpenAI-compatible HTTP | Circuit breaker, test seam |
| `PromptBuilder` | System/user prompt factory | Static, pure functions |
| `ResponseParser` | Lenient JSON parser | Degrades safely, never throws |
| `ActionGuard` | Allow-list + confidence gate | Immutable, mode-aware |
| `AIAgentConfig` | Configuration record | From properties, env vars |
| `AIMode` | Trust ladder enum | ADVISORY → SEMI_AUTO → FULL_AUTO |
| `AIAnalysis` | Structured analysis result | Records, safe defaults |
| `ReportAudience` | Report voice enum | USER / DEV / BOSS |

### Control Flow

1. **Pre-filter** (`AIAgentEngine.isObviouslyHealthy`): If heap < 50%, CPU
   < 0.30, zero SBB errors, no alarms/spunks/leaks — skip the AI call.
   Zero token cost on a quiet system.

2. **Analyze** (`AIAdvisor.analyze`): System prompt + snapshot JSON →
   chat-completions → structured `AIAnalysis`.

3. **Guard** (`ActionGuard.executable`): Filter recommendations through:
   - **Allow-list**: Only 6 actions (`TRIGGER_RELIEF`, `ENABLE_AUTO_RECONFIG`,
     `DISABLE_AUTO_RECONFIG`, `RAISE_ALARM`, `INVESTIGATE`, `NONE`).
   - **Confidence threshold**: configurable (default 0.70).
   - **Mode gating**:
     - `ADVISORY`: Nothing executes, ever.
     - `SEMI_AUTO`: Passive actions execute at threshold; mutating actions
       require ≥ 0.85 confidence.
     - `FULL_AUTO`: All allow-listed actions above threshold execute.

4. **Act** (`AIAgentEngine.execute`): Switch on action name → poke guardian,

## Configuration

### Property Keys (`microjainslee.ai.*`)

| Key | Default | Description |
|---|---|---|
| `enabled` | `false` | Must be `true` to start the agent |
| `api-key` | (env) | API key; falls back to `MICROJAINSLEE_AI_API_KEY` then `DEEPSEEK_API_KEY` |
| `base-url` | `https://api.deepseek.com/v1` | OpenAI-compatible endpoint |
| `model` | `deepseek-chat` | Model name sent in requests |
| `mode` | `ADVISORY` | `ADVISORY`, `SEMI_AUTO`, or `FULL_AUTO` |
| `interval-seconds` | `60` | Loop cycle interval |
| `timeout-seconds` | `15` | HTTP connect/read timeout |
| `confidence-threshold` | `0.70` | Minimum confidence for action execution |
| `action-cooldown-seconds` | `300` | Minimum gap between mutating actions |

### Environment Variables

- `MICROJAINSLEE_AI_API_KEY` — preferred API key source
- `DEEPSEEK_API_KEY` — fallback API key source

## Trust Modes

```
ADVISORY ──► SEMI_AUTO ──► FULL_AUTO
(analyze     (passive      (all validated
 only)        actions +     actions above
              high-conf     threshold)
              mutating)
```

Start at `ADVISORY` and promote only after the agent has earned trust in your
environment. Mode can be changed at runtime via `AIAgentEngine.setMode()`.

## Circuit Breaker

`OpenAiCompatAdvisor` includes a tiny circuit breaker:

- Opens after **3 consecutive failures**.
- Half-opens after **60 seconds**.
- A dead endpoint costs one cheap check per minute instead of a hung loop.

## Design Decisions

### Zero-framework

Pure JDK `java.net.http.HttpClient` + Jackson. No Spring, no Quarkus, no
OkHttp. GraalVM-native friendly. Configuration is supplied via a
`Function<String, String>` lookup so the module stays decoupled from any
particular config source.

### Single-snapshot strategy

Each AI call receives one compact telemetry snapshot (~500–2000 tokens).
This is cheap, fast, and good enough for real-time anomaly detection.
Multi-snapshot history aggregation can come later.

### Safety by construction

- `ActionGuard` is immutable and clamps its threshold to [0, 1].
- `ResponseParser` degrades to `AIAnalysis.unparsed()` with **empty**
  recommendations — nothing can execute off garbage model output.
- `AIMode.parse()` and `ReportAudience.parse()` are lenient and fall back
  to safe defaults (ADVISORY / DEV).

## Package Structure

```
com.microjainslee.ai/
├── AIAdvisor.java           # Pluggable LLM backend interface
├── AIAgentConfig.java       # Configuration record
├── AIAgentEngine.java       # Control loop + runtime API
├── AIAnalysis.java          # Structured analysis result
├── AIMode.java              # Trust ladder enum
├── ActionGuard.java         # Allow-list + confidence gate
├── OpenAiCompatAdvisor.java # OpenAI-compatible HTTP implementation
├── PromptBuilder.java       # System/user prompt factory
├── ReportAudience.java      # Report voice enum
└── ResponseParser.java      # Lenient JSON parser
```

## Usage

```java
// 1. Create configuration
AIAgentConfig config = AIAgentConfig.fromProperties(props::getProperty);
if (!config.hasCredentials()) {
    // agent will stay unavailable
}

// 2. Wire components
OpenAiCompatAdvisor advisor = new OpenAiCompatAdvisor(config);
AIAgentEngine engine = new AIAgentEngine(config, advisor,
        telemetryPort, autonomousGuardian);

// 3. Start the loop
engine.start();

// 4. Runtime control
engine.setMode(AIMode.SEMI_AUTO);
AIAnalysis result = engine.analyzeNow();  // force one cycle
String report = engine.report(ReportAudience.BOSS);

// 5. Shutdown
engine.close();  // AutoCloseable — use try-with-resources
```

## Dependencies

| Dependency | Purpose |
|---|---|
| `jainslee-telemetry` | TelemetrySnapshot data source |
| `jainslee-autonomous` | AutonomousGuardian (optional — nullable) |
| `jackson-databind` 2.18 | JSON serialization |
| `log4j-api` | Logging |

- API key is **never** logged (`AIAgentConfig.toString()` redacts it).
- Configuration string values are trimmed; null/blank values use defaults.

### Virtual threads

`AIAgentEngine` runs its daemon loop on a virtual thread (`Thread.ofVirtual()`),
consistent with micro-jainslee's Java 25 baseline.

### Testability

- `OpenAiCompatAdvisor.HttpTransport` — injectable transport seam.
- `ActionGuard` — pure function, instantiable with any threshold.
- `ResponseParser` / `PromptBuilder` — static methods, no side effects.
- `AIAgentEngine` — accepts any `AIAdvisor` implementation.
- `AiTestFixtures` — shared test doubles for snapshots, ports, completions.

   toggle auto-reconfig, fire alarm. Mutating actions share one cooldown;
   passive actions (alarms) do not.

```

