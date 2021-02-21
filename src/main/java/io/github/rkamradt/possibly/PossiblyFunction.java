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
 *
 * @author randalkamradt
 */
public class PossiblyFunction<V, R> implements Function<V, Possibly<R>> {
    private final ExceptionFunction<V, R> f;
    private PossiblyFunction(final ExceptionFunction<V, R> f) {
        this.f = f;
    }
    static <V, R> PossiblyFunction<V, R> of(final ExceptionFunction<V, R> f) {
        return new PossiblyFunction<>(f);
    }
    @Override
    public Possibly<R> apply(V value) {
        try {
            return Possibly.of(f.apply(value));
        } catch (Exception e) {
            return Possibly.of(e);
        }
    }
}
interface ExceptionFunction<V, R> {
    R apply(V value) throws Exception;
}
