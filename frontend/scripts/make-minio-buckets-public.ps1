$ErrorActionPreference = "Stop"

function Require-Docker {
  if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw "Docker не найден в PATH. Установи Docker Desktop и перезапусти терминал."
  }
}

Require-Docker

$minioUrl = $env:MINIO_URL
if ([string]::IsNullOrWhiteSpace($minioUrl)) {
  # Для контейнера удобно ходить к хосту так
  $minioUrl = "http://host.docker.internal:9000"
}

$accessKey = $env:MINIO_ACCESS_KEY
if ([string]::IsNullOrWhiteSpace($accessKey)) { $accessKey = "minioadmin" }

$secretKey = $env:MINIO_SECRET_KEY
if ([string]::IsNullOrWhiteSpace($secretKey)) { $secretKey = "minioadmin" }

Write-Host "MinIO URL: $minioUrl"
Write-Host "Making buckets public-read: avatars, recipes, comments"

$cmd = @"
mc alias set local $minioUrl $accessKey $secretKey;
mc anonymous set download local/avatars;
mc anonymous set download local/recipes;
mc anonymous set download local/comments;
"@

docker run --rm minio/mc sh -lc $cmd

Write-Host "OK. Теперь прямые URL вида http://localhost:9000/<bucket>/compressed/<file> должны открываться без AccessDenied."

