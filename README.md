# Warsztat debugowania Javy

Praktyczny warsztat: 5 niezależnych stacji. Każda to moduł Mavena z celowo wprowadzonym
błędem i weryfikowalnym celem. Zadanie polega na znalezieniu przyczyny **debuggerem** —
nie przez `System.out.println`.

## Wymagania
- JDK 21 (samo JDK — **Mavena nie trzeba instalować**, jest w repo jako wrapper).
  Wrapper używa JDK wskazanego przez `JAVA_HOME` (a gdy pusty — domyślnego `java`),
  więc upewnij się, że to 21+: `java -version`. Jeśli masz kilka JDK, ustaw `JAVA_HOME`
  na katalog JDK 21 (Windows: zmienne środowiskowe; Linux/macOS: `export JAVA_HOME=...`).
  Build i tak wymusi to enforcerem z czytelnym komunikatem.
- IntelliJ IDEA (lub inne IDE z *Remote JVM Debug*)
- Docker + Compose (stacje 03 i 04)
- Serwer/VPS do postawienia instancji CTF (stacja 04) — opcjonalnie; można też uruchomić lokalnie

## Setup (raz, przed warsztatem)
Nie instalujesz Mavena — repo zawiera Maven Wrapper, który przy pierwszym uruchomieniu
sam pobierze właściwą wersję. Uruchom skrypt budujący:

```bash
# Linux / macOS
./build.sh
```
```bat
REM Windows
build.cmd
```
(Pod spodem to `./mvnw clean install` / `mvnw.cmd clean install`.)

> **Zrób to raz, z dostępem do internetu, PRZED warsztatem.** Pierwsze uruchomienie
> pobiera Maven (przez wrapper), zależności do cache `~/.m2` oraz obraz Dockera dla
> stacji 03 (`eclipse-temurin:21-jre`). Po tym kroku warsztat działa offline — na sali
> internet nie jest już potrzebny. Jeśli Docker jest niedostępny, skrypt pominie obraz
> i dokończy build (Docker jest potrzebny tylko na stacji 03).

Build musi być zielony. Testy demonstrujące błędy są oznaczone `@Disabled("WARSZTAT: ...")`
— dlatego całość się buduje. Zadanie na każdej stacji: zdejmij `@Disabled`, zobacz czerwony
test, znajdź przyczynę debuggerem, napraw, uruchom test ponownie.

Wszędzie, gdzie w instrukcjach pojawia się `mvn`, możesz użyć wrappera: `./mvnw`
(Linux/macOS) lub `mvnw.cmd` (Windows). Stacja 05 ma gotowy skrypt `debug-processor.sh`
/ `debug-processor.cmd`, który odpala tryb debugowania jednym poleceniem.

## Stacje

| # | Moduł | Technika | Instrukcja |
|---|-------|----------|------------|
| 01 | `01-silent-exception` | exception breakpoint, *Evaluate* | [ZADANIE](01-silent-exception/ZADANIE.md) |
| 02 | `02-conditional-breakpoints` | breakpoint warunkowy, *hit count* | [ZADANIE](02-conditional-breakpoints/ZADANIE.md) |
| 03 | `03-in-docker` | JDWP w kontenerze, compose, *Remote JVM Debug* | [ZADANIE](03-in-docker/ZADANIE.md) |
| 04 | `04-jdwp-ctf` | Remote debug 2-w-1: znajdź bug na żywym procesie, potem wykradnij sekret (JDWP) + debrief | [ZADANIE](04-jdwp-ctf/ZADANIE.md) |
| 05 | `05-mvn-annotation-processor` | `mvnDebug`, procesor adnotacji w czasie builda | [ZADANIE](05-mvn-annotation-processor/ZADANIE.md) |

## Sugerowana kolejność
01 → 02 → 03 → 04 → 05. Każda stacja jest jednak samodzielna i można robić je w dowolnej kolejności.
