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

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author randalkamradt
 */
public class PossiblyConsumerTest {
    public static final String GOOD_VALUE = "good";
    public static final String BAD_VALUE = "bad";
    public PossiblyConsumerTest() {
    }

    @Test
    public void testAccept() {
        System.out.println("accept");
        AtomicReference<Exception> ex = new AtomicReference();
        Stream.of(GOOD_VALUE, BAD_VALUE)
                .peek(PossiblyConsumer.of(s -> mapWithException(s), e -> ex.set(e)))
                .collect(Collectors.toList());
        assertEquals("bad value", ex.get().getMessage());
        Stream.of(GOOD_VALUE, BAD_VALUE) // test with null exception consumer
                .peek(PossiblyConsumer.of(s -> mapWithException(s)))
                .collect(Collectors.toList());
        try {
            Stream.of(GOOD_VALUE, BAD_VALUE)
                .peek(PossiblyConsumer.of(s -> mapWithException(s), 
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
    
    private void mapWithException(String value) throws Exception {
        if("bad".equals(value)) {
            throw new Exception("bad value");
        }
    }
}
