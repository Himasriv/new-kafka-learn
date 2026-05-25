# Spring Security Auth Demo

A complete Spring Boot project that demonstrates:
- Authentication with username/password + JWT token
- Authorization with role-based access (`USER` and `ADMIN`)
- Public, user-only, and admin-only REST endpoints

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 in-memory database
- JJWT (JWT token handling)

## Project Structure
- `src/main/java/com/example/securitydemo/config` - security and startup config
- `src/main/java/com/example/securitydemo/security` - JWT and user details logic
- `src/main/java/com/example/securitydemo/auth` - register/login API and service
- `src/main/java/com/example/securitydemo/controller` - protected/public API endpoints
- `src/main/java/com/example/securitydemo/user` - user entity and repository
- `src/test/java/com/example/securitydemo` - integration tests for auth flow and authorization

## Default Admin Account
A startup initializer creates this account if missing:
- username: `admin`
- password: `admin123`

## Run Locally
```powershell
Set-Location "C:\Users\HimasriPeteti\workspace\spring-security-auth-demo"
mvn spring-boot:run
```

## Run Tests
```powershell
Set-Location "C:\Users\HimasriPeteti\workspace\spring-security-auth-demo"
mvn test
```

## API Endpoints
- `GET /api/public/hello` - public endpoint
- `POST /api/auth/register` - register (`USER` role)
- `POST /api/auth/login` - authenticate and get JWT
- `GET /api/user/profile` - requires `USER` or `ADMIN`
- `GET /api/admin/dashboard` - requires `ADMIN`

## Quick Try (PowerShell)
Register user:
```powershell
$registerBody = @{ username = "alice"; password = "password123" } | ConvertTo-Json
$registerResponse = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/register" -ContentType "application/json" -Body $registerBody
$token = $registerResponse.token
$token
```

Call user endpoint with token:
```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/user/profile" -Headers @{ Authorization = "Bearer $token" }
```

Call admin endpoint with user token (expected forbidden):
```powershell
Invoke-WebRequest -Method Get -Uri "http://localhost:8080/api/admin/dashboard" -Headers @{ Authorization = "Bearer $token" }
```

Login as admin and call admin endpoint:
```powershell
$adminLogin = @{ username = "admin"; password = "admin123" } | ConvertTo-Json
$adminResponse = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $adminLogin
$adminToken = $adminResponse.token
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/admin/dashboard" -Headers @{ Authorization = "Bearer $adminToken" }
```

