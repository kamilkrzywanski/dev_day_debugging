package com.foobar.showme.needle;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountEngineTest {

    /**
     * CEL: 10 000 zamówień po 100.0, rabat 10% → oczekiwana suma 900 000.0.
     * Jedno zamówienie psuje wynik. Znajdź je conditional breakpointem (HINTS.md).
     */
    @Disabled("WARSZTAT: zdejmij po naprawie rateFor()")
    @Test
    void kazdeZamowienieMaRabat10Procent() {
        List<Order> orders = new ArrayList<>();
        for (int id = 1; id <= 10_000; id++) {
            orders.add(new Order(id, 100.0));
        }

        double total = new DiscountEngine().totalAfterDiscount(orders);

        assertEquals(900_000.0, total, 0.001,
                "Jedno zamówienie dostaje zły rabat i zaniża sumę");
    }
}
