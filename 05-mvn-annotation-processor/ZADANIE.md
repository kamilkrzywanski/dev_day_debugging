# Stacja 05 — Debug procesora adnotacji przez `mvnDebug`

**Technika:** `mvnDebug` (JDWP na porcie 8000, `suspend=y`), *Remote JVM Debug*, oraz
wariant `-Dmaven.surefire.debug` do debugowania samego testu.

## Cel
Procesor adnotacji generuje w czasie kompilacji klasę `ItemDescription`. Jej metoda
`typeOf("id")` zwraca `"String"` zamiast `"int"`. Zdebuguj **procesor** (kod działający
w trakcie builda), znajdź przyczynę i napraw.

## Objaw
`ItemDescriptionTest` oczekuje realnych typów pól, a dla `id` i `price` dostaje `"String"`.

## Debug procesora (kod uruchamiany w czasie builda)

**Najprościej** (skrypt robi za Ciebie build procesora + start w trybie debug):
```bash
./debug-processor.sh      # Linux / macOS
debug-processor.cmd       # Windows
```
Potem podłącz IDE (*Remote JVM Debug* → port `8000`). Ręcznie krok po kroku poniżej.

Linux / macOS:
```bash
mvn -q -pl 05-mvn-annotation-processor/processor -am install   # zbuduj procesor
cd 05-mvn-annotation-processor/example
mvnDebug clean test          # zawiesza się: "Listening for transport dt_socket at address: 8000"
```

Windows (PowerShell):
```powershell
mvn -q -pl 05-mvn-annotation-processor/processor -am install
cd 05-mvn-annotation-processor\example
mvnDebug.cmd clean test
```

Następnie: IntelliJ → *Remote JVM Debug* → port `8000` → Debug. Ustaw breakpoint w
`DescribeProcessor.mapType(...)`. Build ruszy i zatrzyma się w procesorze.

## Debug testu (inny wariant, do porównania)
```bash
mvn -pl 05-mvn-annotation-processor/example test -Dmaven.surefire.debug
```
To wystawia JDWP dla forka Surefire (domyślnie port 5005), a nie dla procesu Mavena.

## Podpowiedzi
<details><summary>Podpowiedź 1</summary>

`typeOf` zwraca poprawne nazwy pól, ale zły typ → błąd jest w tym, JAK procesor ustala
typ, a nie w szablonie generowanego kodu.
</details>

<details><summary>Podpowiedź 2</summary>

Breakpoint w `mapType(VariableElement field)`. W *Evaluate* sprawdź
`field.asType().toString()` — zobaczysz prawdziwy typ (`int`), a metoda i tak zwraca
zaszyty `"String"`.
</details>

<details><summary>Podpowiedź 3 (rozwiązanie)</summary>

Zmień `mapType` na `return field.asType().toString();`. Przebuduj procesor (`install`),
uruchom ponownie moduł `example`, zdejmij `@Disabled` — test przejdzie.

Uwaga: `asType().toString()` dla typów referencyjnych zwraca pełną nazwę
(np. `java.lang.String`), a dla prymitywów krótką (`int`, `double`).
</details>

## Weryfikacja
Zdjęty `@Disabled` + zielony `ItemDescriptionTest`.
