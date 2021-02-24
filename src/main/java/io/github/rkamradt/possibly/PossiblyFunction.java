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

import java.util.function.Function;

/**
 * A replacement for Function that will return a Possibly to contain a value
 * or an exception.This will allow using methods that throw a checked
 exception to be used in a lambda
 * @author randal kamradt
 * @param <V> the type to map
 * @param <R> the type to return
 * @since 1.0.0
 */
public class PossiblyFunction<V, R> implements Function<V, Possibly<R>> {
    /**
     * Replacement for Function type except it adds throws Exception to the apply method.
     * Used to allow a method that throws a function. This class wraps this
     * interface to create a Possibly type with a value or and exception
     */
    private final ExceptionFunction<V, R> f;
    /** 
     * Create a PossiblyFunction that wraps the ExceptionFunction
     * @param f the ExceptionFunction to wrap
     */
    private PossiblyFunction(final ExceptionFunction<V, R> f) {
        this.f = f;
    }
    /**
     * used to publicly create a PossiblyFunction
     * @param <V> The type of value to map
     * @param <R> The type of return value that will be wrapped inside a Possibly
     * @param f The wrapped function
     * @return A new PossiblyFunction
     */
    static public <V, R> PossiblyFunction<V, R> of(final ExceptionFunction<V, R> f) {
        return new PossiblyFunction<>(f);
    }
    /** 
     * Override of the Function.apply
     * @param value the value to map
     * @return A Possibly with the mapped value or an exception
     */
    @Override
    public Possibly<R> apply(V value) {
        try {
            return Possibly.of(f.apply(value));
        } catch (Exception e) {
            return Possibly.of(e);
        }
    }
    /**
     * A Function that allows checked exceptions
     * @param <V> The type of value to map
     * @param <R> The type of mapped value
     */
    @FunctionalInterface
    public interface ExceptionFunction<V, R> {
        /**
         * Apply the function to the value
         * @param value the value to map
         * @return the mapped value
         * @throws Exception to be caught by the wrapping class
         */
        R apply(V value) throws Exception;
    }
}
