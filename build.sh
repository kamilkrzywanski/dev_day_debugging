#!/usr/bin/env bash
# Setup/weryfikacja: buduje wszystkie stacje i rozgrzewa cache. Nie musisz mieć
# zainstalowanego Mavena — wrapper (./mvnw) pobierze właściwą wersję przy pierwszym
# uruchomieniu. Uruchom RAZ z internetem przed warsztatem; potem działa offline.
set -euo pipefail
cd "$(dirname "$0")"

./mvnw clean install "$@"

# Rozgrzewka obrazu Dockera dla stacji 03 (opcjonalne — nie blokuje builda).
IMAGE="eclipse-temurin:21-jre"
if command -v docker >/dev/null 2>&1 && docker info >/dev/null 2>&1; then
  echo ">> Pobieram obraz Dockera dla stacji 03: $IMAGE"
  docker pull "$IMAGE" || echo "!! Nie udało się pobrać $IMAGE (pomiń, jeśli nie robisz stacji 03)."
else
  echo ">> Docker niedostępny — pomijam obraz (potrzebny tylko na stacji 03)."
fi
echo ">> Gotowe. Warsztat jest gotowy do pracy offline."
