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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author randalkamradt
 */
public class PossiblySupplierTest {
    public static final String GOOD_VALUE = "good";
    public static final String BAD_VALUE = "bad";
    public PossiblySupplierTest() {
    }
    public AtomicInteger state = new AtomicInteger(0);
    @Test
    public void testGet() {
        System.out.println("get");
        List<Possibly<Integer>> list = 
                Stream.generate(PossiblySupplier.of(() -> supplyWithException()))
                .limit(4)
                .collect(Collectors.toList());
        assertEquals(0, list.get(0).getValue().get());
        assertTrue(list.get(1).exceptional());
        assertEquals(2, list.get(2).getValue().get());
        assertTrue(list.get(3).exceptional());
    }
    
    private int supplyWithException() throws Exception {
        int ret = state.getAndIncrement();
        if((ret & 1) == 0) return ret;
        throw new Exception("odd number");
    }
}
