# Telemetry GUI — Steampunk Dashboard

> **Module:** `jainslee-telemetry-vertx`
> **Theme:** Steampunk × Cyberpunk fusion — đồng thau, đồng đỏ, nền tối, điểm nhấn neon
> **Delivery:** Một file `index.html` + `telemetry.js`, không cần bước build, được phục vụ qua Vert.x StaticHandler

---

## Tổng quan

Telemetry dashboard là một giao diện quan sát thời gian thực chạy trên trình duyệt dành cho
micro-jainslee. Nó hiển thị trực tiếp các chỉ số SBB, đồng hồ đo tài nguyên, bảng trạng thái RA,
bảng cảnh báo (alarm), và biểu đồ sparkline — tất cả theo phong cách steampunk/cyberpunk.

**Các quyết định thiết kế chính:**
- **Một file HTML duy nhất** — không bundler, không webpack, không npm install
- **Không framework** — vanilla JS với một vài hàm trợ giúp; không cồng kềnh React/Vue/Preact
- **Vert.x StaticHandler** — phục vụ từ classpath, không cần web server riêng
- **Polling 2 giây** — `setInterval` đơn giản gọi `/api/telemetry/snapshot`
- **CSS variables** — tùy chỉnh toàn bộ theme qua khối `:root`
- **SVG gauges** — đồng hồ đo dạng cung vẽ thủ công, không phụ thuộc thư viện biểu đồ

### Mô tả Ảnh chụp màn hình

<p align="center"><img src="../images/telemetry-gui-1.svg" width="800"/></p>

---

## Phân tích Bố cục

### Thanh trên cùng
- **Tiêu đề:** "micro-jainslee TELEMETRY" với biểu tượng bánh răng
- **Chỉ báo Auto-Reconfig:** Chấm xanh nhấp nháy khi bật, xám khi tắt
- **Đồng hồ:** Hiển thị thời gian snapshot cuối cùng, cập nhật theo dữ liệu

### Thẻ Thống kê (Hàng 1)
Bốn thẻ số lớn để xem nhanh tình trạng:

| Thẻ | Nguồn Dữ liệu | Định dạng |
|------|-----------|--------|
| Active SBBs | `snapshot.sbbs[].active` tổng | Số lớn |
| Events/sec | `snapshot.sbbs[].eps` tổng | Định dạng có dấu phẩy (1,234) |
| Errors (1min) | `snapshot.recentErrors` số lượng trong 60s qua | Đỏ nếu > 0 |
| Uptime | `snapshot.resources.uptimeSeconds` | Định dạng `2h 34m` |

### Đồng hồ đo dạng Cung (Hàng 2)
Đồng hồ đo bán nguyệt vẽ bằng SVG cho mức tiêu thụ tài nguyên:

| Đồng hồ | Dữ liệu | Màu sắc |
|-------|------|--------|
| Heap Usage | `heapUsagePercent` | Xanh ≤70%, hổ phách ≤85%, đỏ >85% |
| CPU Load | `cpuLoad × 100` | Xanh ≤50%, hổ phách ≤80%, đỏ >80% |

Mỗi đồng hồ hiển thị:
- Đoạn cung (đã dùng = đầy, còn lại = trống)
- Văn bản phần trăm ở giữa
- Phụ đề với giá trị tuyệt đối (ví dụ: "128 / 512 MB")

### Bảng Hiệu suất SBB (Hàng 3)
Các hàng theo từng loại SBB với:
- **Tên loại SBB**
- **Sparkline** — biểu đồ SVG nhỏ nội tuyến của 30 mẫu EPS cuối cùng (1 chấm mỗi giây)
- **Số lượng active**
- **Chấm trạng thái** — xanh (healthy - khỏe), hổ phách (phát hiện spunk), đỏ (lỗi)

### Bảng Trạng thái RA (Hàng 4)
Các hàng theo từng RA với:
- **Tên RA**
- **Chấm trạng thái** — xanh (ACTIVE), đỏ (ERROR), xám (INACTIVE)
- **Cổng** — địa chỉ bind
- **Sự kiện đã kích hoạt** (đã định dạng)
- **Lệnh đã nhận** (đã định dạng)

### Bảng Cảnh báo (Hàng 5)

| Phần tử | Mô tả |
|---------|-------------|
| Tiêu đề | "ALARMS" với huy hiệu số lượng đang active và liên kết [history] |
| Màu hàng | Xanh=INFO, Hổ phách=WARNING, Đỏ=CRITICAL, Tím=FATAL |
| Nội dung hàng | Biểu tượng mức độ + timestamp + thông điệp |
| Hành động | Nút [ACK] → POST /api/telemetry/alarms/{id}/acknowledge |
| Trạng thái rỗng | "No active alarms" bằng chữ xanh |

### Bảng Cấu hình (Hàng 6)
Các điều khiển slider/toggle phản ánh `application.properties`:
- Bật/tắt Auto-reconfig (gọi POST /api/telemetry/reconfig)
- Ngưỡng cảnh báo bộ nhớ (75–95%)
- Ngưỡng cảnh báo CPU (50–95%)
- Ngưỡng bão lỗi (50–500/min)
- Ngưỡng cảnh báo trễ (1–30 min)

---

## Cơ chế Cập nhật Thời gian Thực

### Chính: Polling 2 Giây

```javascript
// telemetry.js
const POLL_INTERVAL = 2000;  // 2 seconds

async function fetchSnapshot() {
    const res = await fetch('/api/telemetry/snapshot');
    const data = await res.json();
    render(data);
}

// Start loop
setInterval(fetchSnapshot, POLL_INTERVAL);
fetchSnapshot();  // immediate first fetch
```

### Render Có So sánh Khác biệt

Để giảm thiểu thay đổi DOM, hàm render sử dụng dirty-checking:

```javascript
function render(data) {
    // Only update elements whose values actually changed
    if (data.resources.heapUsagePercent !== lastData.resources.heapUsagePercent) {
        updateGauge('heap-gauge', data.resources.heapUsagePercent);
    }
    // ... per-element dirty checks
    lastData = data;
}
```

### Trạng thái Kết nối

Một chấm nhỏ trên thanh trên cùng nhấp nháy xanh khi lần fetch cuối thành công, chuyển hổ phách sau
2 lần fetch thất bại (4s), đỏ sau 5 lần fetch thất bại (10s) kèm chữ "CONNECTION LOST".

---

## Giải thích Thành phần Đồng hồ đo

### Triển khai SVG Arc Gauge

```html
<svg viewBox="0 0 120 70" class="gauge">
    <!-- Background arc (unfilled) -->
    <path d="M 10 60 A 50 50 0 0 1 110 60"
          fill="none" stroke="var(--gauge-bg)" stroke-width="12"
          stroke-linecap="round" />
    <!-- Filled arc -->
    <path d="M 10 60 A 50 50 0 0 1 110 60"
          fill="none" stroke="var(--gauge-fill)" stroke-width="12"
          stroke-linecap="round"
          stroke-dasharray="157"
          stroke-dashoffset="${157 * (1 - percent/100)}" />
    <!-- Center text -->
    <text x="60" y="45" text-anchor="middle" class="gauge-value">25%</text>
    <text x="60" y="58" text-anchor="middle" class="gauge-label">128 / 512 MB</text>
</svg>
```

### Chuyển đổi Màu sắc

Màu tô sử dụng CSS custom properties thay đổi dựa trên ngưỡng:

```css
.gauge[data-level="safe"]    { --gauge-fill: var(--color-green); }
.gauge[data-level="warning"] { --gauge-fill: var(--color-amber); }
.gauge[data-level="danger"]  { --gauge-fill: var(--color-red); }
```

JavaScript đặt `data-level` sau mỗi lần fetch:

```javascript
function updateGauge(id, percent) {
    const el = document.getElementById(id);
    el.dataset.level = percent > 85 ? 'danger'
                     : percent > 70 ? 'warning'
                     : 'safe';
    // update arc stroke-dashoffset
    el.querySelector('.fill-arc')
      .style.strokeDashoffset = 157 * (1 - percent / 100);
    // update text
    el.querySelector('.gauge-value').textContent = Math.round(percent) + '%';
}
```

---

## Quy trình Xác nhận Cảnh báo (Alarm Acknowledgment)

<p align="center"><img src="../images/telemetry-gui-2.svg" width="800"/></p>

Các cảnh báo đã xác nhận có thể được xem qua liên kết [history], gọi đến
`/api/telemetry/alarms/history?minutes=60`.

---

## Sử dụng Bảng Cấu hình

Bảng cấu hình cho phép người vận hành điều chỉnh ngưỡng trong thời gian chạy mà không cần sửa
`application.properties` hay khởi động lại container.

```javascript
async function updateConfig(key, value) {
    await fetch('/api/telemetry/reconfig', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ [key]: value })
    });
}

// Example: memory threshold slider
memorySlider.addEventListener('change', () => {
    updateConfig('memory.warning-threshold', memorySlider.value);
});
```

Thay đổi có hiệu lực ngay lập tức trong AutoReconfigEngine (không cần khởi động lại).

---

## Tùy chỉnh: CSS Variables

Sửa khối `:root` trong `index.html` để thay đổi toàn bộ theme:

```css
:root {
    /* ── Steampunk Palette ── */
    --bg-primary:        #1a1a2e;    /* deep navy background */
    --bg-secondary:      #16213e;    /* card background */
    --bg-tertiary:       #0f3460;    /* panel header */
    --text-primary:      #e0c097;    /* brass text */
    --text-secondary:    #c9b37e;    /* muted brass */
    --accent-brass:      #d4a843;    /* highlights, borders */
    --accent-copper:     #b87333;    /* secondary accents */
    --accent-neon:       #00ffcc;    /* neon cyan for data */

    /* ── Status Colors ── */
    --color-green:       #2ecc71;
    --color-amber:       #f39c12;
    --color-red:         #e74c3c;
    --color-purple:      #9b59b6;    /* FATAL */

    /* ── Gauge ── */
    --gauge-bg:          #2a2a4a;
    --gauge-fill:        var(--color-green);

    /* ── Typography ── */
    --font-mono:         'JetBrains Mono', 'Fira Code', monospace;
    --font-sans:         'Segoe UI', system-ui, sans-serif;

    /* ── Borders ── */
    --border-color:      #3a3a5a;
    --border-radius:     6px;

    /* ── Shadows ── */
    --card-shadow:       0 2px 8px rgba(0, 0, 0, 0.4);
    --glow-green:        0 0 12px rgba(46, 204, 113, 0.3);
    --glow-red:          0 0 12px rgba(231, 76, 60, 0.3);
}
```

### Hoán đổi Theme Nhanh

**Dark Cyberpunk:**
```css
--bg-primary: #0d0d0d;
--text-primary: #00ff41;
--accent-neon: #ff00ff;
```

**Light Industrial:**
```css
--bg-primary: #f5f0e8;
--text-primary: #3d3226;
--accent-brass: #8b6914;
```

---

## Cấu trúc File

<p align="center"><img src="../images/telemetry-gui-3.svg" width="800"/></p>

**index.html** (< 15 KB đã nén gzip):
- `<style>` nội tuyến chứa toàn bộ CSS
- Cấu trúc HTML ngữ nghĩa với data attributes
- Mẫu SVG gauge (ẩn, được JS sao chép)
- `<script type="module" src="telemetry.js"></script>`

**telemetry.js** (< 8 KB đã nén gzip):
- `fetchSnapshot()` — vòng lặp polling 2s
- `render(data)` — cập nhật DOM có dirty-check
- `updateGauge()`, `updateSparkline()`, `renderAlarms()`, `renderRas()`
- Trình xử lý sự kiện slider cấu hình
- Giám sát tình trạng kết nối

**Không phụ thuộc bên ngoài.** Không tải từ CDN. Hoạt động hoàn toàn ngoại tuyến.
