# 🎨 Design Idea: Test Quarkus/JAINSLEE host WAR file

> **Mục tiêu:** Kiểm tra Quarkus + micro-jainslee app có thể host WAR/serve static content
> để sau này dùng làm giao diện web (HTML/CSS/JS) cho JAIN SLEE app.

---

## 🔍 Câu hỏi cần trả lời

1. Quarkus + jainslee có serve được static file (HTML/JS/CSS) không?
2. WAR file có deploy được trong Quarkus app không?
3. Làm sao để browser truy cập giao diện?

---

## ✅ Câu trả lời ngắn: **CÓ — 3 cách**

| Cách | Mô tả | Dùng khi |
|------|-------|----------|
| **A. Static resources** | Đặt file trong `META-INF/resources/` | Giao diện tĩnh, SPA |
| **B. Quarkus REST** | `@Path` controller trả về HTML/JSON | API + UI động |
| **C. Undertow Servlet** | `quarkus-undertow` + WAR/Servlet | App servlet cũ, JSP |

---

## 🏗️ Phương án A: Static resources (nhanh nhất, khuyến nghị test ngay)

```
example/hello-world-web/
├── pom.xml
└── src/main/
    ├── java/com/example/helloworld/
    │   ├── HelloWorldMain.java         ← MicroSleeContainer + ra-http-server
    │   └── HelloWorldBootstrap.java    ← Wire SBB + RA
    └── resources/
        └── META-INF/
            └── resources/
                └── index.html          ← Hello World UI
```

Flow:
```
Browser → GET http://localhost:8080/
       → Quarkus Undertow phục vụ META-INF/resources/index.html
       
Browser → POST http://localhost:8081/api/ussd/begin
       → ra-http-server → SBB → xử lý logic
```

## 🧪 Test plan

1. Tạo Quarkus app với `index.html` trong `META-INF/resources/`
2. Thêm `ra-http-server` listen port khác (8081)
3. Verify: browser mở `localhost:8080` → thấy HTML
4. Verify: curl POST `localhost:8081/api/ussd/begin` → JAIN SLEE xử lý
