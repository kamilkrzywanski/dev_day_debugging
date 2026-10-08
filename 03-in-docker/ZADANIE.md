# Stacja 03 — Debug aplikacji w kontenerze Dockera

**Technika:** JDWP w kontenerze, `JAVA_TOOL_OPTIONS`, mapowanie portu debuggera,
*Remote JVM Debug* w IDE.

## Cel
Aplikacja liczy cenę brutto. Uruchomiona lokalnie zwraca poprawny wynik (VAT 23%),
a w kontenerze pokazuje cenę równą netto (VAT 0%). Podłącz debugger do procesu
**w kontenerze** i ustal, dlaczego. Potem napraw obraz.

## Uruchomienie lokalne (IDE)
Uruchom `PriceApp` z *Working directory* ustawionym na katalog modułu `03-in-docker`
(żeby `./config/app.properties` był widoczny). Wynik: `brutto=123.00`.

## Uruchomienie w kontenerze
```bash
mvn -q -pl 03-in-docker -am package
cd 03-in-docker
docker compose up --build
```
Logi pokażą `brutto=100.00` (VAT 0%).

## Podłączenie debuggera
IntelliJ → *Run → Edit Configurations → + → Remote JVM Debug* → host `localhost`,
port `5005` → Debug. Ustaw breakpoint w `PriceApp.loadTaxRate()`.

## Podpowiedzi
<details><summary>Podpowiedź 1</summary>

Lokalnie konfiguracja jest widoczna, w kontenerze najwyraźniej nie. Sprawdź, którą
gałąź `loadTaxRate()` wykonuje proces w kontenerze.
</details>

<details><summary>Podpowiedź 2</summary>

Breakpoint na sprawdzeniu istnienia pliku konfiguracyjnego. W kontenerze warunek jest
prawdziwy → zwracana jest stawka `0.0`. W *Evaluate* sprawdź ścieżkę bezwzględną
pliku — zobaczysz, gdzie proces go szuka i że w obrazie go nie ma.
</details>

<details><summary>Podpowiedź 3 (rozwiązanie)</summary>

`Dockerfile` kopiuje tylko `app.jar`, bez katalogu `config/`. Dołóż
`COPY config /opt/app/config` (albo zamontuj wolumen w `compose.yaml`). Po naprawie
logi pokażą `brutto=123.00`.
</details>

## Pułapki do omówienia
- `address=*:5005` (Java 9+) vs samo `5005`.
- `suspend=y` + healthcheck = kontener nigdy nie wstanie (czeka na debugger).
- Hot-swap nie zadziała, gdy klasy w obrazie ≠ klasy w IDE.
- *Working directory* w konfiguracji uruchomienia lokalnego.
