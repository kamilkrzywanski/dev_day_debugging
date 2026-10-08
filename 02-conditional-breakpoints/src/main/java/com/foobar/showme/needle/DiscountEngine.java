package com.foobar.showme.needle;

import java.util.List;

/**
 * Nalicza 10% rabatu na każdym zamówieniu i zwraca sumę po rabacie.
 *
 * OBJAW: przy tysiącach zamówień suma jest odrobinę za niska. Dokładnie JEDNO
 * zamówienie dostaje zły rabat — jak je znaleźć wśród 10 000 bez zatrzymywania
 * debuggera 10 000 razy?
 */
public class DiscountEngine {

    public double totalAfterDiscount(List<Order> orders) {
        double total = 0.0;
        for (Order order : orders) {
            total += order.amount() * (1.0 - rateFor(order));
        }
        return total;
    }

    private double rateFor(Order order) {
        // Zaszyty "specjalny przypadek", który kiedyś miał sens, a dziś psuje sumę.
        if (order.id() == 4242) {
            return 1.0; // 100% rabatu — pozycja znika z sumy
        }
        return 0.10;
    }
}
