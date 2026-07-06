/* ═══════════════════════════════════════════════════════════════
   monitoring.js — micro-jainslee Monitoring Window
   Tabs: Telemetry · Autonomous · AI Agent
   Vanilla JS, no framework, no build step, no dependencies.
   Gracefully degrades: any tab whose backend module is not
   installed (404) shows an install hint instead of breaking.
   ═══════════════════════════════════════════════════════════════ */
(function () {
  'use strict';

  var TELEMETRY_API = '/api/telemetry';
  var AUTONOMOUS_API = '/api/autonomous';
  var AI_API = '/api/ai';
  var POLL_MS = 2000;
  var SLOW_POLL_MS = 5000;
  var MAX_SPARKLINE = 60;

  var $ = function (sel) { return document.querySelector(sel); };
  var $$ = function (sel) { return document.querySelectorAll(sel); };

  var sparklineData = [];
  var startedAt = Date.now();
  var autonomousMissing = false;
  var aiMissing = false;

  // ── helpers ──────────────────────────────────────────────────

  function esc(str) {
    return String(str == null ? '' : str)
      .replace(/&/g, '&amp;').replace(/</g, '&lt;')
      .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }

  function timeAgo(ts) {
    if (!ts) return '--';
    var sec = Math.floor((Date.now() - ts) / 1000);
    if (sec < 0) return 'just now';
    if (sec < 60) return sec + 's ago';
    if (sec < 3600) return Math.floor(sec / 60) + 'm ago';
    return Math.floor(sec / 3600) + 'h ago';
  }

  function setText(sel, value) {
    var el = $(sel);
    if (el) el.textContent = value;
  }

  function pop(sel, value) {
    var el = $(sel);
    if (!el) return;
    if (el.textContent !== String(value)) {
      el.textContent = value;
      el.classList.add('value-changed');
      setTimeout(function () { el.classList.remove('value-changed'); }, 300);
    }
  }

  // ── tabs ─────────────────────────────────────────────────────

  function activateTab(name) {
    $$('.tab-btn').forEach(function (b) {
      b.classList.toggle('active', b.getAttribute('data-tab') === name);
    });
    $$('.tab-panel').forEach(function (p) {
      p.classList.toggle('active', p.id === 'tab-' + name);
    });
    try { localStorage.setItem('mw-tab', name); } catch (e) { /* private mode */ }
  }

  $$('.tab-btn').forEach(function (btn) {
    btn.addEventListener('click', function () {
      activateTab(btn.getAttribute('data-tab'));
    });
  });
  try {
    var saved = localStorage.getItem('mw-tab');
    if (saved) activateTab(saved);
  } catch (e) { /* ignore */ }

  // ── clock / uptime / online dot ──────────────────────────────

  function updateClock() {
    var d = new Date();
    setText('#clock', d.toLocaleTimeString('en-GB'));
  }

  function updateUptime(seconds) {
    var s = Math.floor(seconds);
    var parts = [];
    if (s >= 86400) parts.push(Math.floor(s / 86400) + 'd');
    if (s >= 3600) parts.push(String(Math.floor((s % 86400) / 3600)).padStart(2, '0') + 'h');
    parts.push(String(Math.floor((s % 3600) / 60)).padStart(2, '0') + 'm');
    parts.push(String(s % 60).padStart(2, '0') + 's');
    setText('#uptime-display', 'Uptime: ' + parts.join(' '));
  }

  function setOnline(ok) {
    var dot = $('#status-dot');
    var label = $('#status-label');
    dot.className = ok ? 'online' : 'offline';
    dot.id = 'status-dot';
    label.textContent = ok ? 'SYSTEM ONLINE' : 'CONNECTION LOST';
    label.style.color = ok ? 'var(--neon-green)' : 'var(--neon-red)';
  }

  // ═══ TAB 1: TELEMETRY ══════════════════════════════════════════

  function pollTelemetry() {
    var indicator = $('#refresh-indicator');
    indicator.classList.add('spinning');
    fetch(TELEMETRY_API + '/snapshot')
      .then(function (res) {
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
      })
      .then(function (snap) {
        renderTelemetry(snap);
        setOnline(true);
      })
      .catch(function (err) {
        console.warn('telemetry fetch failed:', err.message);
        setOnline(false);
      })
      .finally(function () { indicator.classList.remove('spinning'); });
  }

  function renderTelemetry(snap) {
    var sbbs = snap.sbbs || [];
    var ras = snap.ras || [];
    var res = snap.resources || {};

    // overview tiles
    var total = 0, eps = 0, errors = 0, p99 = 0;
    sbbs.forEach(function (s) {
      total += s.active || 0;
      eps += s.eps || 0;
      errors += s.errors || 0;
      p99 = Math.max(p99, s.p99us || 0);
    });
    pop('#sbb-total', total);
    pop('#sbb-eps', Math.round(eps));
    pop('#sbb-latency', Math.round(p99) + 'µs');
    pop('#sbb-errors', errors);

    // per-type table
    var tbody = $('#sbb-table-body');
    tbody.innerHTML = sbbs.length === 0
      ? '<tr><td colspan="5" style="text-align:center;color:var(--text-dim)">No SBBs registered</td></tr>'
      : sbbs.map(function (s) {
          var cls = (s.errors || 0) > 10 ? 'row-error'
                  : (s.spunks || 0) > 0 || (s.errors || 0) > 0 ? 'row-spunk' : '';
          return '<tr class="' + cls + '">'
            + '<td>' + esc(s.sbbType) + '</td>'
            + '<td>' + (s.active || 0) + '</td>'
            + '<td>' + Math.round(s.eps || 0) + '</td>'
            + '<td>' + Math.round(s.p99us || 0) + 'µs</td>'
            + '<td>' + (s.errors || 0) + '</td></tr>';
        }).join('');

    // gauges
    var cpuPct = Math.round((res.cpuLoad || 0) * 100);
    var cpuFill = $('#cpu-gauge-fill');
    cpuFill.style.setProperty('--gauge-pct', cpuPct);
    cpuFill.className = 'gauge-arc-fill' + (cpuPct > 90 ? ' danger' : cpuPct > 70 ? ' warn' : '');
    setText('#cpu-value', cpuPct + '%');
    $('#cpu-value').className = 'gauge-value' + (cpuPct > 90 ? ' over-threshold' : '');

    var ramPct = Math.round(res.heapUsagePercent || 0);
    var ramFill = $('#ram-gauge-fill');
    ramFill.style.setProperty('--gauge-pct', ramPct);
    ramFill.className = 'gauge-arc-fill' + (ramPct > 90 ? ' danger' : ramPct > 70 ? ' warn' : '');
    setText('#ram-value', (res.heapUsedMb || 0) + ' / ' + (res.heapMaxMb || 0) + ' MB');
    $('#ram-value').className = 'gauge-value' + (ramPct > 90 ? ' over-threshold' : '');

    pop('#thread-value', res.virtualThreads || 0);

    // sparkline
    sparklineData.push(eps);
    if (sparklineData.length > MAX_SPARKLINE) sparklineData.shift();
    var max = Math.max.apply(null, sparklineData.concat([1]));
    $('#sparkline').innerHTML = sparklineData.map(function (v) {
      return '<div class="spark-bar" style="height:' + Math.round((v / max) * 100) + '%"></div>';
    }).join('');
    setText('#sparkline-value', Math.round(eps) + ' eps');

    // RA table
    var raBody = $('#ra-table-body');
    raBody.innerHTML = ras.length === 0
      ? '<tr><td colspan="4" style="text-align:center;color:var(--text-dim)">No RAs registered</td></tr>'
      : ras.map(function (ra) {
          var st = (ra.state || 'unknown').toLowerCase();
          var dot = st === 'active' ? 'active' : st === 'error' ? 'error' : 'inactive';
          return '<tr><td><span class="state-dot ' + dot + '"></span>' + esc(ra.raName) + '</td>'
            + '<td>' + esc(ra.state) + '</td>'
            + '<td>' + esc(ra.port || '—') + '</td>'
            + '<td>' + (ra.eventsFired || 0) + '</td></tr>';
        }).join('');

    // uptime: prefer the longest-lived RA, else page uptime
    var raUp = 0;
    ras.forEach(function (ra) { raUp = Math.max(raUp, ra.uptimeSeconds || 0); });
    updateUptime(raUp > 0 ? raUp : (Date.now() - startedAt) / 1000);

    // alarms
    var alarms = snap.activeAlarms || [];
    var list = $('#alarms-list');
    list.innerHTML = alarms.length === 0
      ? '<div class="empty-note">✓ No active alarms</div>'
      : alarms.map(function (a) {
          var lv = (a.level || 'INFO').toLowerCase();
          return '<div class="alarm-item level-' + lv + '">'
            + '<span class="alarm-badge level-' + lv + '">' + esc(a.level) + '</span>'
            + '<span class="alarm-source">' + esc(a.source) + '</span>'
            + '<span class="alarm-time">' + timeAgo(a.timestamp) + '</span>'
            + '<span class="alarm-msg">' + esc(a.message) + '</span>'
            + '<button class="alarm-clear" data-id="' + esc(a.id) + '">CLEAR</button>'
            + '</div>';
        }).join('');
    list.querySelectorAll('.alarm-clear').forEach(function (btn) {
      btn.addEventListener('click', function () {
        fetch(TELEMETRY_API + '/alarms/' + encodeURIComponent(btn.getAttribute('data-id')) + '/clear',
          { method: 'POST' }).then(pollTelemetry);
      });
    });

    // custom metrics
    var metrics = snap.customMetrics || [];
    $('#custom-metrics-list').innerHTML = metrics.length === 0
      ? '<span class="empty-note">No app-defined metrics yet</span>'
      : metrics.map(function (m) {
          var val = m.isGauge ? (m.gaugeValue || 0) : (m.counterValue || 0);
          return '<div class="custom-metric-row">'
            + '<span class="metric-icon">' + (m.isGauge ? '📈' : '📊') + '</span>'
            + '<span class="metric-name">' + esc(m.name) + '</span>'
            + '<span class="metric-value">' + val + '</span></div>';
        }).join('');

    // auto-reconfig toggle reflects server state
    if (typeof snap.autoReconfigEnabled === 'boolean') {
      var t = $('#auto-reconfig-toggle');
      if (t && document.activeElement !== t) {
        t.checked = snap.autoReconfigEnabled;
        updateReconfigLabel(snap.autoReconfigEnabled);
      }
    }

    // tab dot: red if any critical/fatal alarm, amber if any alarm, green otherwise
    var dotCls = 'tab-dot green';
    if (alarms.some(function (a) { return a.level === 'CRITICAL' || a.level === 'FATAL'; })) dotCls = 'tab-dot red';
    else if (alarms.length > 0) dotCls = 'tab-dot amber';
    $('#dot-telemetry').className = dotCls;
  }

  function updateReconfigLabel(on) {
    var el = $('#reconfig-status');
    el.textContent = on ? 'ENABLED' : 'DISABLED';
    el.className = 'toggle-status' + (on ? ' active' : '');
  }

  $('#auto-reconfig-toggle').addEventListener('change', function () {
    var on = this.checked;
    updateReconfigLabel(on);
    fetch(TELEMETRY_API + '/config', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ autoReconfig: on })
    }).catch(function (e) { console.warn('config failed:', e); });
  });

  // ═══ TAB 2: AUTONOMOUS ═════════════════════════════════════════

  function pollAutonomous() {
    if (autonomousMissing) return;
    fetch(AUTONOMOUS_API + '/health')
      .then(function (res) {
        if (res.status === 404) { markAutonomousMissing(); return null; }
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
      })
      .then(function (h) { if (h) renderAutonomous(h); })
      .catch(function (err) { console.warn('autonomous fetch failed:', err.message); });
  }

  function markAutonomousMissing() {
    autonomousMissing = true;
    $('#auto-missing-card').style.display = 'block';
    $('#dot-autonomous').className = 'tab-dot';
    setText('#health-light', 'N/A');
  }

  function renderAutonomous(h) {
    var status = (h.status || 'UNKNOWN').toUpperCase();
    var light = $('#health-light');
    light.textContent = status;
    light.className = 'traffic-light ' + (status === 'GREEN' ? 'green'
      : status === 'AMBER' ? 'amber' : status === 'RED' ? 'red' : 'unknown');

    $('#dot-autonomous').className = 'tab-dot ' + (status === 'GREEN' ? 'green'
      : status === 'AMBER' ? 'amber' : status === 'RED' ? 'red' : '');

    var reasons = h.reasons || [];
    $('#health-reasons').innerHTML = reasons.length === 0
      ? '<div class="empty-note" style="padding:6px">✓ all signals nominal</div>'
      : reasons.map(function (r) { return '<div class="reason-item">' + esc(r) + '</div>'; }).join('');

    setText('#auto-heap', (h.heapPct != null ? h.heapPct : '--') + '%');
    setText('#auto-cpu', h.cpuLoad != null ? Math.round(h.cpuLoad * 100) + '%' : '--');
    setText('#auto-errors', h.errors != null ? h.errors : '--');
    setText('#auto-spunks', h.spunks != null ? h.spunks : '--');
    setText('#auto-ts', timeAgo(h.ts));

    setText('#guardian-level', h.guardianLevel || '--');
    setText('#guardian-relief', h.reliefRuns != null ? h.reliefRuns : '--');
    $$('#guardian-ladder > div').forEach(function (row) {
      row.classList.toggle('active-level',
        row.getAttribute('data-level') === h.guardianLevel);
    });
  }

  // ═══ TAB 3: AI AGENT ═══════════════════════════════════════════

  function pollAi() {
    if (aiMissing) return;
    fetch(AI_API + '/status')
      .then(function (res) {
        if (res.status === 404) { markAiMissing(); return null; }
        if (!res.ok) throw new Error('HTTP ' + res.status);
        return res.json();
      })
      .then(function (st) {
        if (!st) return null;
        renderAiStatus(st);
        return fetch(AI_API + '/analysis');
      })
      .then(function (res) {
        if (!res || !res.ok) return null;
        return res.json();
      })
      .then(function (a) { if (a) renderAnalysis(a); })
      .catch(function (err) { console.warn('ai fetch failed:', err.message); });
  }

  function markAiMissing() {
    aiMissing = true;
    $('#ai-missing-card').style.display = 'block';
    $('#dot-ai').className = 'tab-dot';
    $('#ai-analyze-btn').disabled = true;
    $$('.report-btns .btn-brass').forEach(function (b) { b.disabled = true; });
  }

  function renderAiStatus(st) {
    var enToggle = $('#ai-enabled-toggle');
    if (document.activeElement !== enToggle) enToggle.checked = !!st.enabled;
    var enLabel = $('#ai-enabled-status');
    enLabel.textContent = st.enabled ? 'ENABLED' : 'DISABLED';
    enLabel.className = 'toggle-status' + (st.enabled ? ' active' : '');

    setText('#ai-available', st.available ? '✓ ONLINE' : '✗ OFFLINE');
    $('#ai-available').style.color = st.available ? 'var(--neon-green)' : 'var(--neon-red)';
    setText('#ai-model', st.model || '--');
    var pill = $('#ai-mode-pill');
    pill.innerHTML = '<span class="mode-pill ' + (st.mode || 'ADVISORY').toLowerCase() + '">'
      + esc(st.mode) + '</span>';
    var sel = $('#ai-mode-select');
    if (document.activeElement !== sel) sel.value = st.mode || 'ADVISORY';
    setText('#ai-cycles', st.cycles);
    setText('#ai-skipped', st.skippedHealthy);
    setText('#ai-analyses', st.analyses);
    setText('#ai-actions', st.actionsExecuted);

    $('#dot-ai').className = 'tab-dot ' + (st.enabled ? (st.available ? 'green' : 'amber') : '');
  }

  function renderAnalysis(a) {
    if (!a || !a.summary) return;
    setText('#ai-summary', a.summary + (a.timestamp ? '   (' + timeAgo(a.timestamp) + ')' : ''));

    var risks = a.risks || [];
    $('#ai-risks').innerHTML = risks.length === 0
      ? '<span class="empty-note" style="padding:6px">none</span>'
      : risks.map(function (r) {
          var lv = (r.level || 'LOW').toLowerCase();
          return '<div class="risk-item">'
            + '<span class="risk-badge ' + lv + '">' + esc(r.level) + '</span>'
            + '<span>' + esc(r.description) + '</span>'
            + '<span style="color:var(--neon-cyan)">' + Math.round((r.confidence || 0) * 100) + '%</span>'
            + '</div>';
        }).join('');

    var recs = a.recommendations || [];
    $('#ai-recs').innerHTML = recs.length === 0
      ? '<span class="empty-note" style="padding:6px">none</span>'
      : recs.map(function (r) {
          return '<div class="rec-item">'
            + '<span class="rec-action">' + esc(r.action) + '</span>'
            + '<span class="rec-conf">' + Math.round((r.confidence || 0) * 100) + '%</span>'
            + '<div>' + esc(r.reasoning) + '</div>'
            + '</div>';
        }).join('');
  }

  $('#ai-enabled-toggle').addEventListener('change', function () {
    postAiConfig({ enabled: this.checked });
  });
  $('#ai-mode-select').addEventListener('change', function () {
    postAiConfig({ mode: this.value });
  });

  function postAiConfig(body) {
    fetch(AI_API + '/config', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    }).then(pollAi).catch(function (e) { console.warn('ai config failed:', e); });
  }

  $('#ai-analyze-btn').addEventListener('click', function () {
    var btn = this;
    btn.disabled = true;
    btn.textContent = '🔍 Analyzing…';
    fetch(AI_API + '/analyze', { method: 'POST' })
      .then(function (res) { return res.ok ? res.json() : null; })
      .then(function (a) { if (a) renderAnalysis(a); })
      .catch(function (e) { console.warn('analyze failed:', e); })
      .finally(function () {
        btn.disabled = false;
        btn.textContent = '🔍 Analyze Now';
      });
  });

  $$('.report-btns .btn-brass').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var audience = btn.getAttribute('data-audience');
      $('#report-spinner').style.display = 'inline';
      $('#report-output').textContent = 'Generating ' + audience.toUpperCase() + ' report…';
      fetch(AI_API + '/report?audience=' + encodeURIComponent(audience))
        .then(function (res) {
          if (!res.ok) throw new Error('HTTP ' + res.status);
          return res.text();
        })
        .then(function (text) { $('#report-output').textContent = text; })
        .catch(function (e) {
          $('#report-output').textContent = 'Report failed: ' + e.message;
        })
        .finally(function () { $('#report-spinner').style.display = 'none'; });
    });
  });

  // ── init ─────────────────────────────────────────────────────

  updateClock();
  setInterval(updateClock, 1000);
  pollTelemetry();
  pollAutonomous();
  pollAi();
  setInterval(pollTelemetry, POLL_MS);
  setInterval(pollAutonomous, POLL_MS);
  setInterval(pollAi, SLOW_POLL_MS);
})();
