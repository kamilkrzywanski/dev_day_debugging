# Stacja 02 — Igła w stogu siana (breakpoint warunkowy)

**Technika:** breakpoint warunkowy, *hit count*, *Evaluate and log*.

## Cel
Wśród 10 000 zamówień dokładnie jedno dostaje zły rabat i zaniża sumę. Znajdź je,
**nie zatrzymując** debuggera na pozostałych rekordach. Potem napraw kod.

## Objaw
`DiscountEngineTest` oczekuje sumy `900 000.0`, a dostaje wartość odrobinę niższą.

## Kroki
1. Zdejmij `@Disabled` w `DiscountEngineTest` i uruchom (Debug).
2. Ustaw breakpoint w `DiscountEngine.rateFor()` z **warunkiem**, tak aby zatrzymał się
   tylko na rekordzie z błędnym rabatem.
3. Odczytaj `id` winnego zamówienia.
4. Napraw `rateFor()` i uruchom test ponownie.

## Podpowiedzi
<details><summary>Podpowiedź 1</summary>

Suma jest za niska dokładnie o wartość jednego zamówienia → jeden `id` jest traktowany
inaczej niż reszta.
</details>

<details><summary>Podpowiedź 2</summary>

Prawy klik na kropce breakpointu → *Condition*. Wpisz warunek wyłapujący nietypowy
rabat, np. `rateFor != 0.10` (na zwracanej wartości) albo warunek na zbyt niski wynik
pozycji w pętli. Debugger zatrzyma się tylko raz.
</details>

<details><summary>Podpowiedź 3 (rozwiązanie)</summary>

Breakpoint zatrzyma się na jednym konkretnym `id`, które dostaje rabat 100%. Usuń lub
popraw ten zaszyty przypadek w `rateFor()`. Warto też pokazać *hit count* oraz akcję
*Evaluate and log* (logowanie bez zmiany kodu).
</details>

## Weryfikacja
Zdjęty `@Disabled` + zielony `DiscountEngineTest`.
