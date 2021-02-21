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
 *
 * @author randalkamradt
 */
public class PossiblySupplier<T> implements Supplier<Possibly<T>> {
    private final ExceptionSupplier<T> f;
    private PossiblySupplier(final ExceptionSupplier<T> f) {
        this.f = f;
    }
    static <T> PossiblySupplier<T> of(final ExceptionSupplier<T> f) {
        return new PossiblySupplier<>(f);
    }
    @Override
    public Possibly<T> get() {
        try {
            return Possibly.of(f.get());
        } catch (Exception e) {
            return Possibly.of(e);
        }
    }
}
interface ExceptionSupplier<T> {
    T get() throws Exception;
}
