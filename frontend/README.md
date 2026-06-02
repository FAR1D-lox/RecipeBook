# Frontend — Книга рецептов

React + TypeScript + Vite. Зелёная тема, JWT в `localStorage`.

## Запуск

```bash
cd frontend
npm install
npm run dev
```

Откройте http://localhost:5173

По умолчанию API проксируется на `http://localhost:8080` через `/api` (см. `vite.config.ts`).

Для продакшена задайте в `.env`:

```
VITE_API_URL=http://localhost:8080
```

## Маршруты

| Путь | Доступ |
|------|--------|
| `/login`, `/register` | Все |
| `/users/search`, `/users/:id` | Все |
| `/recipes/search`, `/recipes/:id` | Все (интерактив на рецепте — только после входа) |
| `/profile`, `/profile/edit`, `/recipes/new`, `/recipes/:id/edit`, `/recommendations`, `/favorites` | Только авторизованные |

## Доработки бэкенда

Следующие изменения **уже реализованы** в Spring-приложении:

- `CorsConfig` + CORS в `SecurityConfig`
- Гостевой `GET /recipes/{id}` и публичная статистика лайков
- `GET /users/{id}`, `GET /users/me`
- `PUT /recipes/{id}` + `UpdateRecipeDto`

---

## Важно про картинки (MinIO AccessDenied)

Если по прямой ссылке на файл MinIO возвращает:

```xml
<Code>AccessDenied</Code>
```

— значит бакеты MinIO приватные, и `<img src="http://localhost:9000/...">` не сможет их показать.

Так как **бэкенд не выдаёт presigned URL**, нужно сделать бакеты публичными на чтение.

### Автоматически (PowerShell)

Запусти (при работающем MinIO из `docker compose up -d`):

```powershell
cd frontend
.\scripts\make-minio-buckets-public.ps1
```

Скрипт выставит public-read для бакетов:
- `avatars`
- `recipes`
- `comments`

## API, используемый frontend

- `POST /users/register`, `POST /users/login`
- `GET /users/search?username=&cursor=&size=`
- `PUT /users`, `DELETE /users`
- `GET /recipes/{id}`, `GET /recipes/search?query=`, `GET /recipes/author/{authorId}`
- `POST /recipes`, `DELETE /recipes/{id}`, `PUT /recipes/{id}`
- `GET|POST|DELETE /recipes/{id}/likes`, `GET /recipes/{id}/likes/stats`
- `GET|POST|DELETE /recipes/{id}/comments`
- `GET|POST|DELETE /users/favorites`, `DELETE /users/favorites/{favoriteId}`
- `GET /recommendations`
- `POST /upload` (multipart)

Токен: заголовок `Authorization: Bearer <jwt>`.
