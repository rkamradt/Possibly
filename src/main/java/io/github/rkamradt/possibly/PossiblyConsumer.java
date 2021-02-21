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

/**
 * A replacement for Consumer that will return a Possibly to contain a value
 * or an exception.This will allow using methods that throw a checked
 exception to be used in a lambda
 * @author randal kamradt
 * @param <T> the type to accept
 * @since 1.0.0
 */
public class PossiblyConsumer<T> implements Consumer<T> {
    /**
     * Replacement for Consumer type except it adds throws Exception to the accept method.
     * Used to allow a method that throws a function. This class wraps this
     * interface to create a Possibly type with a value or and exception
     */
    private final ExceptionConsumer<T> f;
    private final Consumer<Exception> e; 
    /** 
     * Create a PossiblySuppier that wraps the ExceptionConsumer
     * @param f the ExceptionConsumer to wrap
     */
    private PossiblyConsumer(final ExceptionConsumer<T> f,
            final Consumer<Exception> e) {
        this.f = f;
        this.e = e;
    }
    /**
     * used to publicly create a PossiblyConsumer
     * @param <T> The type of value to accept
     * @param f The wrapped Consumer
     * @param e a consumer for exceptions
     * @return A new PossiblyConsumer
     */
     static <T> PossiblyConsumer<T> of(final ExceptionConsumer<T> f,
             final Consumer<Exception> e) {
        return new PossiblyConsumer<>(f, e);
    }
    /** 
     * Override of the Consumer.accept
     * @param value
     */
    @Override
    public void accept(T value) {
        try {
            f.accept(value);
        } catch (Exception ex) {
            e.accept(ex);
        }
    }
    /**
     * @param <T> The type of value to accept
     */
    @FunctionalInterface
    interface ExceptionConsumer<T> {
        /**
         * accept a value
         * @throws Exception to be caught by the wrapping class
         */
        void accept(T value) throws Exception;
    }
}
