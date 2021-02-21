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

import java.util.function.Supplier;

/**
 * A replacement for Supplier that will return a Possibly to contain a value
 * or an exception.This will allow using methods that throw a checked
 exception to be used in a lambda
 * @author randal kamradt
 * @param <T> the type to supply
 * @since 1.0.0
 */
public class PossiblySupplier<T> implements Supplier<Possibly<T>> {
    /**
     * Replacement for Supplier type except it adds throws Exception to the get method.
     * Used to allow a method that throws a function. This class wraps this
     * interface to create a Possibly type with a value or and exception
     */
    private final ExceptionSupplier<T> f;
    /** 
     * Create a PossiblySuppier that wraps the ExceptionSuppier
     * @param f the ExceptionSupplier to wrap
     */
    private PossiblySupplier(final ExceptionSupplier<T> f) {
        this.f = f;
    }
    /**
     * used to publicly create a PossiblySupplier
     * @param <T> The type of value to supply that will be wrapped in a Possibly
     * @param f The wrapped supplier
     * @return A new PossiblySupplier
     */
     static <T> PossiblySupplier<T> of(final ExceptionSupplier<T> f) {
        return new PossiblySupplier<>(f);
    }
    /** 
     * Override of the Supplier.get
     * @return A Possibly with the mapped value or an exception
     */
    @Override
    public Possibly<T> get() {
        try {
            return Possibly.of(f.get());
        } catch (Exception e) {
            return Possibly.of(e);
        }
    }
    /**
     * A Function that allows checked exceptions
     * @param <T> The type of value to supply
     */
    @FunctionalInterface
    interface ExceptionSupplier<T> {
        /**
         * Get a value from the supplier
         * @return the supplied value
         * @throws Exception to be caught by the wrapping class
         */
        T get() throws Exception;
    }
}
