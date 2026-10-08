package com.foobar.showme.model;

import com.foobar.showme.apt.Describe;

/** Klasa opisywana procesorem. Zwróć uwagę na typy pól — nie każde to String. */
@Describe
public class Item {
    int id;
    String name;
    double price;
}
