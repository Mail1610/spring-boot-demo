# restaurant-order-system

餐廳點餐系統 Spring Boot 專案，涵蓋菜單管理與訂單管理功能。

## 技術棧

- Java 17 + Spring Boot 3
- H2 File-based Database（重啟後資料保留）
- Spring Data JPA / Hibernate
- Lombok
- Bean Validation（Jakarta）

## 快速啟動

```bash
./mvnw spring-boot:run
```

服務預設跑在 `http://localhost:8080`

H2 Console（可視化資料庫）：`http://localhost:8080/h2-console`
- JDBC URL：`jdbc:h2:file:./data/orderdb`
- Username：`sa`
- Password：（空白）

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

---

### 菜單 API — `/api/menu`

#### 取得上架中的菜單
```
GET /api/menu
```
回傳 `available = true` 的品項。

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
Content-Type: application/json

{
  "name": "珍珠奶茶（大）",
  "price": 75.0,
  "category": "飲料",
  "available": true
}
```

#### 刪除菜單品項
```
DELETE /api/menu/{id}
```

---

### 訂單 API — `/api/orders`

#### 取得全部訂單
```
GET /api/orders
```

#### 依狀態篩選訂單
```
GET /api/orders?status=PENDING
```
| status 值 | 說明 |
|-----------|------|
| `PENDING` | 待處理 |
| `PREPARING` | 準備中 |
| `DONE` | 完成 |

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
| 欄位 | 型別 | 必填 | 說明 |
|------|------|------|------|
| tableNumber | String | ✅ | 桌號 |
| items | Array | ✅ | 至少一筆品項 |
| items[].menuItemId | Long | ✅ | 菜單品項 ID |
| items[].quantity | Integer | ✅ | 數量 |

#### 更新訂單狀態
```
PUT /api/orders/{id}/status
Content-Type: application/json

{
  "status": "PREPARING"
}
```

---

## 專案結構

```
src/main/java/com/demo/
├── DemoApplication.java              # 應用程式入口
├── common/
│   ├── ApiResponse.java              # 統一 API 回應格式
│   └── LogUtils.java                 # 統一日誌工具（禁止直接用 System.out / slf4j）
├── config/
│   └── DataInitializer.java          # 啟動時初始化範例資料
├── controller/
│   ├── MenuController.java           # 菜單 CRUD API
│   └── OrderController.java          # 訂單 API
├── dto/
│   ├── request/
│   │   ├── MenuItemRequest.java      # 新增/修改菜單請求
│   │   ├── OrderRequest.java         # 建立訂單請求
│   │   ├── OrderItemRequest.java     # 訂單品項
│   │   └── UpdateOrderStatusRequest.java  # 更新訂單狀態請求
│   └── response/
│       ├── MenuItemResponse.java     # 菜單回應
│       ├── OrderResponse.java        # 訂單回應
│       └── OrderItemResponse.java    # 訂單品項回應
├── entity/
│   ├── BaseEntity.java               # 共用欄位（id、createdAt 等）
│   ├── MenuItem.java                 # 菜單品項實體
│   ├── Order.java                    # 訂單實體
│   ├── OrderItem.java                # 訂單品項實體
│   └── OrderStatus.java             # 訂單狀態 Enum（PENDING / PREPARING / DONE）
├── exception/
│   ├── ResourceNotFoundException.java  # 資源不存在例外
│   └── GlobalExceptionHandler.java     # 統一例外處理
├── repository/
│   ├── MenuItemRepository.java       # 菜單資料存取
│   └── OrderRepository.java          # 訂單資料存取
└── service/
    ├── MenuService.java              # 菜單業務邏輯
    └── OrderService.java             # 訂單業務邏輯

src/main/resources/
├── application.properties            # 應用程式設定
└── static/
    ├── index.html                    # 顧客點餐頁
    ├── kitchen.html                  # 廚房顯示頁
    └── admin.html                    # 後台管理頁
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
- 避免注入其他 Service

### Repository 層
- 只做資料庫操作
- 使用 Spring Data JPA，避免手寫 SQL

### DTO 規則
- Request：record + Bean Validation 做輸入驗證
- Response：record，在 Service 層做 Entity → Response 轉換

### 例外處理
- 資源不存在拋 `ResourceNotFoundException`（由 `GlobalExceptionHandler` 統一轉成 404）
- 不在 Controller 層 try-catch，集中由 `@RestControllerAdvice` 處理

### 日誌規範
- 禁用 `System.out.println()` 以及直接使用 slf4j
- 統一使用（為了哪天 slf4j 有問題要替換元件方便）
    - `LogUtils.info()`
    - `LogUtils.error()`
    - `LogUtils.debug()`
    - `LogUtils.warn()`
