/* ═══════════════════════════════════════════════════════════════
   telemetry.js — Steampunk Telemetry Dashboard Engine
   Vanilla JS, no framework, no dependencies.
   ═══════════════════════════════════════════════════════════════ */
(function() {
  'use strict';

  const API_BASE = '/api/telemetry';
  const POLL_INTERVAL = 2000;
  const MAX_SPARKLINE = 60;

  // ── State ──
  let sparklineData = [];
  let sortCol = 'name';
  let sortAsc = true;

  // ── DOM helpers ──
  const $ = (sel) => document.querySelector(sel);
  const $$ = (sel) => document.querySelectorAll(sel);

  // ── Core fetch loop ──
  async function fetchSnapshot() {
    const indicator = $('#refresh-indicator');
    indicator.classList.add('spinning');
    try {
      const res = await fetch(API_BASE + '/snapshot');
      if (!res.ok) throw new Error('HTTP ' + res.status);
      const snap = await res.json();
      updateAll(snap);
      setStatus(true);
    } catch (err) {
      console.warn('Telemetry fetch failed:', err.message);
      setStatus(false);
    } finally {
      indicator.classList.remove('spinning');
    }
  }

  // ── Master update ──
  function updateAll(snap) {
    updateSbbStats(snap.sbbs || []);
    updateSbbTable(snap.sbbs || []);
    updateGauges(snap.resources || {});
    updateSparkline(snap);
    updateRaTable(snap.ras || []);
    updateAlarms(snap.activeAlarms || []);
    updateCustomMetrics(snap.customMetrics || []);
    // Auto-reconfig toggle
    if (snap.autoReconfigEnabled !== undefined) {
      $('#auto-reconfig-toggle').checked = snap.autoReconfigEnabled;

  // ── SBB Stats ──
  function updateSbbStats(sbbs) {
    const total = sbbs.reduce((s, x) => s + (x.active || 0), 0);
    const eps = sbbs.reduce((s, x) => s + (x.eps || 0), 0);
    const avgLat = sbbs.length > 0
      ? sbbs.reduce((s, x) => s + (x.avgLatencyUs || 0), 0) / sbbs.length : 0;
    const errors = sbbs.reduce((s, x) => s + (x.errorCount || 0), 0);

    animateValue($('#sbb-total'), total);
    animateValue($('#sbb-eps'), Math.round(eps));
    animateValue($('#sbb-latency'), Math.round(avgLat) + 'µs');
    animateValue($('#sbb-errors'), errors);
  }

  // ── SBB Table ──
  function updateSbbTable(sbbs) {
    const tbody = $('#sbb-table-body');
    if (!sbbs || sbbs.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:var(--text-dim)">No SBBs registered</td></tr>';
      return;
    }
    // Sort
    const sorted = [...sbbs].sort((a, b) => {
      let va = a[sortCol], vb = b[sortCol];
      if (typeof va === 'string') va = va.toLowerCase();
      if (typeof vb === 'string') vb = vb.toLowerCase();
      if (va == null) va = 0;
      if (vb == null) vb = 0;
      return sortAsc ? (va > vb ? 1 : va < vb ? -1 : 0)
                     : (va < vb ? 1 : va > vb ? -1 : 0);
    });

    tbody.innerHTML = sorted.map(sbb => {
      let cls = 'row-healthy';
      const ep = sbb.eps || 0;
      const er = sbb.errorCount || 0;
      if (er > 10) cls = 'row-error';
      else if (ep > 1000 || er > 0) cls = 'row-spunk';
      return `<tr class="${cls}">
        <td>${esc(sbb.name || '—')}</td>
        <td>${sbb.active || 0}</td>
        <td>${Math.round(ep)}</td>
        <td>${Math.round(sbb.avgLatencyUs || 0)}µs</td>
        <td>${er}</td>

  // ── Gauges ──
  function updateGauges(res) {
    // CPU
    const cpuPct = Math.round((res.cpuLoad || 0) * 100);
    const cpuFill = $('.cpu-gauge-fill');
    cpuFill.style.setProperty('--gauge-pct', cpuPct);
    cpuFill.className = 'gauge-arc-fill cpu-gauge-fill' +
      (cpuPct > 90 ? ' danger' : cpuPct > 70 ? ' warn' : '');
    $('#cpu-value').textContent = cpuPct + '%';
    $('#cpu-value').className = 'gauge-value' + (cpuPct > 90 ? ' over-threshold' : '');

    // RAM
    const ramPct = Math.round(res.heapUsagePercent || 0);
    const ramFill = $('.ram-gauge-fill');
    ramFill.style.setProperty('--gauge-pct', ramPct);
    ramFill.className = 'gauge-arc-fill ram-gauge-fill' +
      (ramPct > 90 ? ' danger' : ramPct > 70 ? ' warn' : '');
    $('#ram-value').textContent = (res.heapUsedMb || 0) + ' / ' + (res.heapMaxMb || 0) + ' MB';
    $('#ram-value').className = 'gauge-value' + (ramPct > 90 ? ' over-threshold' : '');

    // Threads
    animateValue($('#thread-value'), (res.activeThreads || res.platformThreads || 0) + ' VT');
  }

  // ── Sparkline ──
  function updateSparkline(snap) {
    const eps = snap.eventsPerSec || snap.eventRate || 0;
    sparklineData.push(eps);
    if (sparklineData.length > MAX_SPARKLINE) sparklineData.shift();

    const container = $('#sparkline');
    const max = Math.max(...sparklineData, 1);
    container.innerHTML = sparklineData.map(v => {
      const h = Math.round((v / max) * 100);
      return '<div class="spark-bar" style="height:' + h + '%" title="' + v + ' eps"></div>';
    }).join('');
    $('#sparkline-value').textContent = Math.round(eps) + ' eps';
  }

  // ── RA Table ──
  function updateRaTable(ras) {
    const tbody = $('#ra-table-body');
    if (!ras || ras.length === 0) {
      tbody.innerHTML = '<tr><td colspan="4" style="text-align:center;color:var(--text-dim)">No RAs registered</td></tr>';
      return;
    }
    tbody.innerHTML = ras.map(ra => {
      const state = (ra.state || 'unknown').toLowerCase();
      return '<tr class="ra-row">' +
        '<td><span class="state-dot ' + state + '"></span> ' + esc(ra.name || '—') + '</td>' +
        '<td>' + esc(ra.state || '—') + '</td>' +
        '<td>' + (ra.port || '—') + '</td>' +
        '<td>' + (ra.eventsFired || ra.eventsPerSec || 0) + '/s</td>' +
      '</tr>';
    }).join('');
  }

  // ── Alarms ──
  function updateAlarms(alarms) {
    const container = $('#alarms-list');
    if (!alarms || alarms.length === 0) {
      container.innerHTML = '<div class="no-alarms">✓ No active alarms</div>';
      return;
    }
    container.innerHTML = alarms.map(a => {
      const lv = (a.level || 'INFO').toLowerCase();
      return '<div class="alarm-item level-' + lv + '">' +
        '<span class="alarm-badge level-' + lv + '">' + esc(a.level || 'INFO') + '</span>' +

  // ── Helpers ──
  function animateValue(el, value) {

  // ── Custom Metrics ──
  function updateCustomMetrics(metrics) {
    const el = $('#custom-metrics-list');
    if (!metrics || metrics.length === 0) {
      el.innerHTML = '<span class="no-alarms">No app-defined metrics yet</span>';
      return;
    }
    el.innerHTML = metrics.map(m => {
      var val = m.isGauge ? (m.gaugeValue || 0) : (m.counterValue || 0);
      var icon = m.isGauge ? '📈' : '📊';
      return '<div class="custom-metric-row">' +
        '<span class="metric-icon">' + icon + '</span>' +
        '<span class="metric-name">' + esc(m.name || '?') + '</span>' +
        '<span class="metric-value">' + val + '</span>' +
        '</div>';
    }).join('');
  }

  // ── Helpers ──
    if (!el) return;
    el.textContent = value;
    el.classList.add('value-changed');
    setTimeout(function() { el.classList.remove('value-changed'); }, 300);
  }

  function esc(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;')
      .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
  }

  function escAttr(str) {
    return String(str || '').replace(/\\/g, '\\\\').replace(/'/g, "\\'").replace(/"/g, '&quot;');
  }

  function timeAgo(ts) {
    var sec = Math.floor((Date.now() - ts) / 1000);
    if (sec < 0) return 'just now';
    if (sec < 60) return sec + 's ago';
    if (sec < 3600) return Math.floor(sec / 60) + 'm ago';
    if (sec < 86400) return Math.floor(sec / 3600) + 'h ago';
    return Math.floor(sec / 86400) + 'd ago';
  }

  function updateUptime(seconds) {
    if (!seconds && seconds !== 0) return;
    var d = Math.floor(seconds / 86400);
    var h = Math.floor((seconds % 86400) / 3600);
    var m = Math.floor((seconds % 3600) / 60);
    var s = Math.floor(seconds % 60);
    var parts = [];
    if (d > 0) parts.push(d + 'd');
    if (h > 0 || d > 0) parts.push(String(h).padStart(2, '0') + 'h');
    parts.push(String(m).padStart(2, '0') + 'm');
    parts.push(String(s).padStart(2, '0') + 's');
    $('#uptime-display').textContent = 'Uptime: ' + parts.join(' ');
  }

  function updateToggleLabel(id, checked, onText, offText) {
    var el = $('#' + id);
    if (el) {
      el.textContent = checked ? onText : offText;
      el.className = 'toggle-status' + (checked ? ' active' : '');
    }
  }

  function setStatus(ok) {
    var dot = $('#status-dot');
    var label = $('#status-label');
    if (ok) {
      dot.className = 'status-dot online';
      label.textContent = 'SYSTEM ONLINE';

  // ── Config apply (global) ──
  window.applyConfig = function() {
    var config = {
      autoReconfig: $('#auto-reconfig-toggle').checked,
      sbbPoolMin: parseInt($('#cfg-pool-min').value) || 4,
      sbbPoolMax: parseInt($('#cfg-pool-max').value) || 32,
      bufferSize: parseInt($('#cfg-buffer').value) || 1024,
      virtualThreads: $('#cfg-vt').checked
    };
    fetch(API_BASE + '/config', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(config)
    }).then(function(r) {
      if (r.ok) {
        var btn = $('#btn-apply');
        btn.textContent = '✓ Applied';
        btn.style.borderColor = 'var(--neon-green)';
        btn.style.color = 'var(--neon-green)';
        setTimeout(function() {
          btn.textContent = '⚙ Apply Config';
          btn.style.borderColor = 'var(--border-brass)';
          btn.style.color = 'var(--border-brass)';
        }, 2000);
      }
    }).catch(function(e) { console.warn('applyConfig failed:', e); });
  };

  // ── SBB Table sorting ──
  function setupSorting() {
    var headers = $$('#sbb-table th');
    headers.forEach(function(th) {
      th.addEventListener('click', function() {
        var col = th.getAttribute('data-sort');
        if (!col) return;
        if (sortCol === col) {
          sortAsc = !sortAsc;
        } else {
          sortCol = col;
          sortAsc = true;
        }
        // Re-render from last known data
        if (lastSnap && lastSnap.sbbs) updateSbbTable(lastSnap.sbbs);
      });
    });
  }

  // ── Toggle listeners ──
  function setupToggles() {
    var reconfigToggle = $('#auto-reconfig-toggle');
    if (reconfigToggle) {
      reconfigToggle.addEventListener('change', function() {
        updateToggleLabel('reconfig-status', this.checked, 'ENABLED', 'DISABLED');
      });
    }
    var vtToggle = $('#cfg-vt');
    if (vtToggle) {
      vtToggle.addEventListener('change', function() {
        updateToggleLabel('vt-status', this.checked, 'ON', 'OFF');
      });
    }
  }

  // ── Last snapshot cache for sorting ──
  var lastSnap = null;
  var origUpdateAll = updateAll;
  updateAll = function(snap) {
    lastSnap = snap;
    origUpdateAll(snap);
  };

  // ── Init ──
  setupSorting();
  setupToggles();
  setInterval(fetchSnapshot, POLL_INTERVAL);
  setInterval(updateClock, 1000);
  fetchSnapshot();
  updateClock();
})();
