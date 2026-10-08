package com.foobar.showme.silent;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTotalsTest {

    /**
     * CEL: ten test ma przejść PO naprawie.
     * Zdejmij @Disabled, uruchom, zobacz błędną sumę, a potem znajdź przyczynę
     * debuggerem (patrz HINTS.md) — BEZ dodawania System.out.println.
     */
    @Disabled("WARSZTAT: zdejmij po naprawie parse()")
    @Test
    void sumujeWszystkiePozycje() {
        OrderTotals totals = new OrderTotals();

        double result = totals.sum(List.of("10.00", "5.50", "1,50"));

        assertEquals(17.00, result, 0.001,
                "Pozycja '1,50' (przecinek!) jest po cichu gubiona i liczy się jako 0");
    }
}
