/*
 * The MIT License
 *
 * Copyright 2021 randalkamradt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.rkamradt.possibly;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A replacement for Predicate that will return a Possibly to contain a value
 * or an exception.This will allow using methods that throw a checked
 exception to be used in a lambda
 * @author randal kamradt
 * @param <T> the type to test
 * @since 1.0.0
 */
public class PossiblyPredicate<T> implements Predicate<T> {
    /**
     * Replacement for Predicate type except it adds throws Exception to the apply method.
     * Used to allow a method that throws an exception. This class wraps this
     * interface to create a Possibly type with a value or and exception
     */
    private final ExceptionPredicate<T> f;
    private final Consumer<Exception> e;
    /** 
     * Create a PossiblyPredicate that wraps the ExceptionPredicate
     * @param f the ExceptionPredicate to wrap
     */
    private PossiblyPredicate(final ExceptionPredicate<T> f,
            Consumer<Exception> e) {
        this.f = f;
        this.e = e;
    }
    /**
     * used to publicly create a PossiblyPredicate
     * @param <V> The type of value to test
     * @param f The wrapped Predicate
     * @return A new PossiblyPredicate
     */
    static <T> PossiblyPredicate<T> of(final ExceptionPredicate<T> f,
            final Consumer<Exception> e) {
        return new PossiblyPredicate<>(f, e);
    }
    /**
     * used to publicly create a PossiblyPredicate
     * @param <T> The type of value to test
     * @param f The wrapped Predicate
     * @return A new PossiblyPredicate
     */
    static <T> PossiblyPredicate<T> of(final ExceptionPredicate<T> f) {
        return new PossiblyPredicate<>(f, null);
    }
    /** 
     * Override of the Predicate.test
     * @param value the value to map
     * @return A Possibly with the mapped value or an exception
     */
    @Override
    public boolean test(T value) {
        try {
            return f.test(value);
        } catch (Exception ex) {
            if(e != null) {
                e.accept(ex);
            }
            return false;
        }
    }
    /**
     * A Predicate that allows checked exceptions
     * @param <T> The type of value test
     */
    @FunctionalInterface
    public interface ExceptionPredicate<T> {
        /**
         * Test a value and return true or false
         * @param value the value to map
         * @return the mapped value
         * @throws Exception to be caught by the wrapping class
         */
        boolean test(T value) throws Exception;
    }
}
