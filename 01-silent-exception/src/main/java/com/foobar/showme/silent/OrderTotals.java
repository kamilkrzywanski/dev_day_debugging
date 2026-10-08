package com.foobar.showme.silent;

import java.util.List;

/**
 * Sumuje ceny pozycji zamówienia podane jako tekst.
 *
 * OBJAW: dla pewnych danych suma jest za niska, ale NIC się nie wywala —
 * żadnego wyjątku, żadnego logu. Klasyczny "połknięty" wyjątek.
 */
public class OrderTotals {

    public double sum(List<String> prices) {
        double total = 0.0;
        for (String price : prices) {
            total += parse(price);
        }
        return total;
    }

    private double parse(String price) {
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            // Ktoś kiedyś "uciszył" ten wyjątek. Pozycja po prostu znika z sumy.
            return 0.0;
        }
    }
}
