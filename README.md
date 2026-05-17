# restaurant-order-system

## 系統簡介

**restaurant-order-system** 是一套以 Spring Boot 3 開發的餐廳點餐管理系統，提供**客人點餐**與**店員後台**兩套完全隔離的使用介面。

### 系統架構

```
客人系統（公開）                   店員系統（需登入）
─────────────────────             ─────────────────────────────
瀏覽菜單（依分類篩選）  ──訂單──▶  廚房顯示（即時查看待處理訂單）
加入購物車                          更新訂單狀態（待處理→準備中→完成）
填寫桌號後送出訂單                  後台管理（菜單 CRUD、訂單總覽）
```

### 核心功能

| 功能 | 說明 |
|------|------|
| 菜單瀏覽 | 客人依分類瀏覽上架品項，含圖片與描述 |
| 購物車 | 加減數量、即時顯示小計與總計 |
| 送出訂單 | 填寫桌號後送出，系統即時通知廚房 |
| 廚房顯示 | 店員查看訂單清單，逐步更新處理狀態 |
| 菜單管理 | 店員新增／編輯／下架品項 |
| 資料持久化 | H2 File-based DB，重啟後資料保留 |

### 安全隔離

- 客人系統完全公開，無需帳號
- 店員頁面（`/kitchen.html`、`/admin.html`）及店員 API 由 **Spring Security** 保護
- 未登入時直接輸入後台網址，系統自動導向登入頁

---

餐廳點餐系統，分為**客人系統**與**店員系統**兩套獨立介面。

## 技術棧

- Java 17 + Spring Boot 3
- Spring Security（登入保護店員系統）
- H2 File-based Database（重啟後資料保留）
- Spring Data JPA / Hibernate
- Lombok
- Bean Validation（Jakarta）

## 快速啟動

```bash
mvn spring-boot:run
```

服務預設跑在 `http://localhost:8080`

---

## 系統入口

### 客人系統（公開，不需登入）

| 頁面 | URL | 說明 |
|------|-----|------|
| 點餐頁 | `http://localhost:8080/` | 瀏覽菜單、加入購物車、送出訂單 |

### 店員系統（需要登入）

| 頁面 | URL | 說明 |
|------|-----|------|
| 廚房顯示 | `http://localhost:8080/kitchen.html` | 查看待處理訂單、更新訂單狀態 |
| 後台管理 | `http://localhost:8080/admin.html` | 菜單管理、訂單總覽 |

登入帳號在 `application.properties` 設定：

```properties
app.admin.username=admin
app.admin.password=admin123
```

---

## 資料庫 Console

瀏覽器開 `http://localhost:8080/h2-console`（需登入後才可存取）

- JDBC URL：`jdbc:h2:file:./data/orderdb`
- Username：`sa`
- Password：`123456`

---

## API 文檔

所有回應皆包裝在統一的 `ApiResponse` 結構：

```json
{
  "success": true,
  "data": { ... },
  "error": null
}
```

### 客人可用 API

#### 取得上架中的菜單
```
GET /api/menu
```

#### 建立訂單
```
POST /api/orders
Content-Type: application/json

{
  "tableNumber": "A3",
  "items": [
    { "menuItemId": 1, "quantity": 2 },
    { "menuItemId": 3, "quantity": 1 }
  ]
}
```

---

### 店員專用 API（需登入）

#### 取得全部訂單
```
GET /api/orders
GET /api/orders?status=PENDING
```

| status 值 | 說明 |
|-----------|------|
| `PENDING` | 待處理 |
| `PREPARING` | 準備中 |
| `DONE` | 完成 |

#### 更新訂單狀態
```
PUT /api/orders/{id}/status
Content-Type: application/json

{ "status": "PREPARING" }
```

#### 取得全部菜單（含下架）
```
GET /api/menu/all
```

#### 新增菜單品項
```
POST /api/menu
Content-Type: application/json

{
  "name": "珍珠奶茶",
  "description": "招牌飲品",
  "price": 60.0,
  "category": "飲料",
  "imageUrl": "https://example.com/img.jpg",
  "available": true
}
```

| 欄位 | 型別 | 必填 | 說明 |
|------|------|------|------|
| name | String | ✅ | 品項名稱 |
| description | String | ❌ | 描述 |
| price | BigDecimal | ✅ | 價格（≥ 0） |
| category | String | ✅ | 分類 |
| imageUrl | String | ❌ | 圖片連結 |
| available | boolean | ✅ | 是否上架 |

#### 修改菜單品項
```
PUT /api/menu/{id}
```

#### 刪除菜單品項
```
DELETE /api/menu/{id}
```

---

## 專案結構

```
src/main/java/com/demo/
├── RestaurantOrderApplication.java       # 應用程式入口
├── common/
│   ├── ApiResponse.java                  # 統一 API 回應格式
│   └── LogUtils.java                     # 統一日誌工具
├── config/
│   ├── DataInitializer.java              # 啟動時初始化範例資料
│   └── SecurityConfig.java               # Spring Security 存取控制設定
├── controller/
│   ├── MenuController.java               # 菜單 CRUD API
│   └── OrderController.java              # 訂單 API
├── dto/
│   ├── request/
│   │   ├── MenuItemRequest.java
│   │   ├── OrderRequest.java
│   │   ├── OrderItemRequest.java
│   │   └── UpdateOrderStatusRequest.java
│   └── response/
│       ├── MenuItemResponse.java
│       ├── OrderResponse.java
│       └── OrderItemResponse.java
├── entity/
│   ├── BaseEntity.java
│   ├── MenuItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── OrderStatus.java                  # PENDING / PREPARING / DONE
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── MenuItemRepository.java
│   └── OrderRepository.java
└── service/
    ├── MenuService.java
    └── OrderService.java

src/main/resources/
├── application.properties
└── static/
    ├── index.html                        # 客人點餐頁（公開）
    ├── kitchen.html                      # 廚房顯示頁（需登入）
    └── admin.html                        # 後台管理頁（需登入）
```

---

## 撰寫規則

### Controller 層
- 只做為資料出入口，業務邏輯移到 Service
- 只注入一個對應的 Service
- POST / PUT 端點加上 `@Valid` 做輸入驗證
- 使用 `@RequiredArgsConstructor` 取代 `@Autowired`

### Service 層
- 進行業務邏輯處理
- 可調用 Repository 進行資料庫操作

### Repository 層
- 只做資料庫操作
- 使用 Spring Data JPA，避免手寫 SQL

### DTO 規則
- Request：record + Bean Validation
- Response：record，在 Service 層做 Entity → Response 轉換

### 例外處理
- 資源不存在拋 `ResourceNotFoundException`（統一轉成 404）
- 不在 Controller 層 try-catch，集中由 `@RestControllerAdvice` 處理

### 日誌規範
- 禁用 `System.out.println()` 及直接使用 slf4j
- 統一使用 `LogUtils.info()` / `LogUtils.error()` / `LogUtils.debug()` / `LogUtils.warn()`
