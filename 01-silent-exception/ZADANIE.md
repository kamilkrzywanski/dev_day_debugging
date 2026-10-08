# Stacja 01 — Połknięty wyjątek

**Technika:** breakpoint na wyjątku (także złapanym w `catch`), okno *Evaluate*, *Drop Frame*.

## Cel
Test `OrderTotalsTest` liczy błędną sumę zamówienia. Znajdź przyczynę **wyłącznie
debuggerem** — bez modyfikacji kodu i bez `System.out.println`. Dopiero potem napraw kod.

## Objaw
Suma pozycji jest za niska, ale nic się nie wywala — brak wyjątku, brak logu.

## Kroki
1. Otwórz `OrderTotalsTest` i zdejmij `@Disabled`.
2. Uruchom test (Debug) — zobaczysz, że wynik ≠ oczekiwana wartość.
3. Ustaw breakpoint na wyjątku i prześledź, gdzie znika brakująca kwota.
4. Napraw `OrderTotals` i uruchom test ponownie — ma być zielony.

## Podpowiedzi
<details><summary>Podpowiedź 1</summary>

Skoro suma jest za niska, a nic nie leci na wierzch — gdzieś jest `catch`, który
połyka wyjątek. Znajdź, która pozycja go wywołuje.
</details>

<details><summary>Podpowiedź 2</summary>

IntelliJ → *Run → View Breakpoints* (Ctrl+Shift+F8) → **+** → *Java Exception Breakpoint*
→ `java.lang.NumberFormatException`. Zaznacz **Caught exception**. Uruchom test w trybie Debug.
</details>

<details><summary>Podpowiedź 3 (rozwiązanie)</summary>

Debugger zatrzyma się w metodzie `parse()` na pozycji z przecinkiem zamiast kropki.
`Double.parseDouble` rzuca `NumberFormatException`, a `catch` zwraca `0.0`, więc pozycja
znika z sumy. Popraw parsowanie (np. normalizacja separatora dziesiętnego) i zamień
„cichy" `catch` na świadomą obsługę.
</details>

## Weryfikacja
Zdjęty `@Disabled` + zielony `OrderTotalsTest`.
