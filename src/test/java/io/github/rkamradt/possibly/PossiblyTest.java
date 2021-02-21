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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author randalkamradt
 */
public class PossiblyTest {
    
    public PossiblyTest() {
    }

    @Test
    public void testEmpty() {
        System.out.println("empty");
        Possibly result = Possibly.empty();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIs() {
        System.out.println("is");
        String value = "test";
        Possibly instance = Possibly.of(value);
        assertTrue(instance.is());
    }

    @Test
    public void testExceptional() {
        System.out.println("exceptional");
        String value = "test";
        Possibly instance = Possibly.of(new Exception(value));
        assertTrue(instance.exceptional());
    }

    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        String value = "test";
        Possibly instance = Possibly.empty();
        assertTrue(instance.isEmpty());
        instance = Possibly.of(value)
                .filter(v -> !v.equals(value));
        assertTrue(instance.isEmpty());
        instance = Possibly.ofNullable(null);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testDoOnException() {
        System.out.println("doOnException");
        String value = "test";
        Exception expected = new Exception(value);
        Possibly<String> instance = Possibly.of(expected);
        AtomicReference<Exception> ex = new AtomicReference();
        instance.doOnException(e -> ex.set(e));
        assertEquals(expected, ex.get());
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String value = "test";
        Possibly instance = Possibly.of(value);
        assertTrue(instance.getValue().isPresent());
        assertEquals(value, instance.getValue().get());
    }

    @Test
    public void testGetException() {
        System.out.println("getException");
        String value = "test";
        Exception expected = new Exception(value);
        Possibly<String> instance = Possibly.of(expected);
        assertTrue(instance.getException().isPresent());
        assertEquals(expected, instance.getException().get());
    }

    @Test
    public void testMap() {
        System.out.println("map");
        String svalue = "100";
        Possibly<String> instance = Possibly.of(svalue);
        assertEquals(100, instance.map(s -> Integer.valueOf(s)).getValue().get());
    }

    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
        String value = "test";
        Possibly<String> instance = Possibly.of(value);
        assertEquals(value, instance.flatMap(v -> Optional.of(v)).getValue().get());
    }

    @Test
    public void testFilter() {
        System.out.println("filter");
        String value = "test";
        Possibly<String> instance = Possibly.of(value);
        assertEquals(value, instance
                .filter(s -> s.equals(value))
                .getValue().get());
        assertTrue(instance
                .filter(s -> !s.equals(value))
                .isEmpty());
    }

    @Test
    public void testStream() {
        System.out.println("stream");
        String value = "test";
        Stream<String> instance = Possibly.of(value).stream();
        assertEquals(value, instance.findAny().get());
        instance = Possibly.<String>empty().stream();
        assertEquals(0, instance.count());
    }
    
}
