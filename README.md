# Spring Boot Demo Template

可重複使用的 Spring Boot 專案模板，內建完整 CRUD 範例。

## 技術棧
- Java 17
- Spring Boot 3.3
- Spring Data JPA
- H2 in-memory DB（零設定，改 MySQL/PostgreSQL 只需改 `application.properties`）
- Lombok
- Maven

## 快速啟動

```bash
./mvnw spring-boot:run
```

或在 IntelliJ 直接執行 `DemoApplication.java`。

## API 端點

| Method | URL | 說明 |
|--------|-----|------|
| GET | `/api/items` | 取得所有項目（可加 `?search=keyword`） |
| GET | `/api/items/{id}` | 取得單一項目 |
| POST | `/api/items` | 新增項目 |
| PUT | `/api/items/{id}` | 更新項目 |
| DELETE | `/api/items/{id}` | 刪除項目 |

## H2 資料庫控制台

啟動後開啟：http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:demodb`
- Username: `sa`
- Password: (空白)

## 新增新功能的步驟


## Git 初始化

```bash
git init
git add .
git commit -m "initial commit"
git remote add origin <你的 GitHub URL>
git push -u origin main
```
