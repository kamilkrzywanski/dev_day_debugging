package com.foobar.showme.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Oznacz klasę tą adnotacją, a procesor wygeneruje dla niej klasę {@code <Name>Description}. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Describe {
}
