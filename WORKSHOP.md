# Plan warsztatu (ok. 90 min)

| Czas | Blok | Treść |
|------|------|-------|
| 0:00–0:10 | Intro | Po co debugger zamiast `println`. Setup: `mvn clean install`. |
| 0:10–0:25 | Stacja 01 | Połknięty wyjątek — exception breakpoint, *Evaluate*, *Drop Frame*. |
| 0:25–0:40 | Stacja 02 | Igła w stogu — breakpoint warunkowy, *hit count*, *Evaluate and log*. |
| 0:40–1:00 | Stacja 03 | „U mnie działa" — JDWP w Dockerze, *Remote JVM Debug* do kontenera. |
| 1:00–1:15 | Stacja 04 | Remote debug 2-w-1: znajdź bug na żywym procesie, potem wykradnij sekret (CTF) + debrief bezpieczeństwa JDWP. |
| 1:15–1:30 | Stacja 05 | `mvnDebug` — debug procesora adnotacji uruchamianego w czasie builda (finał nawiązujący do prelekcji). |

## Zasady prowadzenia
- Każda stacja: uczestnicy najpierw próbują sami (podpowiedzi 1–2 w `ZADANIE.md`),
  poziom 3 to rozwiązanie omawiane na tablicy.
- Nie zdradzaj przyczyny z góry — wartość daje samodzielne dojście debuggerem.
- Trzymaj czas: kto utknie, dostaje podpowiedź 2, zamiast siedzieć 15 min.

## Checklist prowadzącego
- [ ] `mvn clean install` przechodzi na maszynie prowadzącego i na kilku laptopach (Linux/Windows).
- [ ] Docker lub Podman działa; obraz `eclipse-temurin:21-jre` ściągnięty wcześniej (stacja 03).
- [ ] Stacja 04: serwer(y) CTF postawione (patrz deploy/README.md), środowisko efemeryczne do wyrzucenia.
- [ ] Rozwiązania pod ręką (poziom 3 w `ZADANIE.md`).

## Wymagania techniczne (Docker lub Podman)
Stacje 03 i 04 używają kontenerów. Wystarczy sam silnik — **Docker Desktop nie jest
wymagany**. Działa Docker Engine + `docker compose` v2 albo Podman + `podman compose`.

Smoke test przed warsztatem (silnik realnie uruchamia kontenery i obraz się ściąga —
samo `--version` tego nie potwierdza):
```bash
# Docker
docker run --rm hello-world
docker pull eclipse-temurin:21-jre

# Podman (odpowiednik; pełne nazwy omijają pytanie o rejestr)
podman run --rm docker.io/library/hello-world
podman pull docker.io/library/eclipse-temurin:21-jre
```
Uwagi dla Podmana:
- W instrukcjach stacji 03 wpisuj `podman compose …` zamiast `docker compose …`
  (albo doinstaluj `podman-docker`, wtedy `docker` działa jako alias).
- Mac/Windows: najpierw `podman machine start` (na Linuksie niepotrzebne).
- Port debuggera 5005 jest >1024, więc rootless Podman nie wymaga nic ekstra.
