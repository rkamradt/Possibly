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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author randalkamradt
 */
public class PossiblyPredicateTest {
    public static final String GOOD_VALUE = "good";
    public static final String BAD_VALUE = "bad";
    public PossiblyPredicateTest() {
    }

    @Test
    public void testTest() {
        System.out.println("test");
        System.out.println("accept");
        AtomicReference<Exception> ex = new AtomicReference();
        List<String> list = Stream.of(GOOD_VALUE, BAD_VALUE)
                .filter(PossiblyPredicate.of(s -> mapWithException(s), e -> ex.set(e)))
                .collect(Collectors.toList());
        assertEquals("bad value", ex.get().getMessage());
        assertEquals(1, list.size());
        assertEquals(GOOD_VALUE, list.get(0));
        list = Stream.of(GOOD_VALUE, BAD_VALUE) // test with null exception consumer
                .filter(PossiblyPredicate.of(s -> mapWithException(s)))
                .collect(Collectors.toList());
        assertEquals(1, list.size());
        assertEquals(GOOD_VALUE, list.get(0));
        try {
            Stream.of(GOOD_VALUE, BAD_VALUE)
                .filter(PossiblyPredicate.of(s -> mapWithException(s), 
                        e -> {
                            throw new RuntimeException("runtime exception", e);
                }))
                .collect(Collectors.toList());
            fail("expected exception not thrown");
        } catch(RuntimeException re) {
            assertEquals("runtime exception", re.getMessage());
            assertEquals("bad value", re.getCause().getMessage());
        }
    }
    
    private boolean mapWithException(String value) throws Exception {
        if("bad".equals(value)) {
            throw new Exception("bad value");
        }
        return true;
    }
}
