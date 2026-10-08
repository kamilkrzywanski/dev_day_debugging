package com.foobar.showme.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CEL: wygenerowana klasa ItemDescription ma zwracać realne typy pól.
 * Teraz dla 'id' zwraca "String" zamiast "int". Zdebuguj PROCESOR (mvnDebug) — HINTS.md.
 */
class ItemDescriptionTest {

    @Test
    void generujeRealneTypyPol() {
        assertEquals("int", ItemDescription.typeOf("id"));
        assertEquals("java.lang.String", ItemDescription.typeOf("name"));
        assertEquals("double", ItemDescription.typeOf("price"));
    }
}
