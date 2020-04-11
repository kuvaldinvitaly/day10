/**
 * Интерфейс для поиска элементов на старницах PageObject в рантайме
 */
package ru.lanit.atschool.Intefaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NameOfElement
{
    String value() default "";
}
