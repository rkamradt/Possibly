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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p>This type represents an object that is created by a function that could
 * produce an Exception.It is similar to the Scala Either class, except that
 * their can only be a value or an Exception rather than the more vague left and
 * right values. It it patterned on the Java Optional type, and both the value 
 * or the Exception is presented as an Optional
 *
 * <p>It is intended to be used by the functional interfaces included in this 
 * library to allow a function that throws a checked exception in a lambda 
 * expression without having to deal with the exception immediately.
 *
 * <p>For example the code 
 *
 * <pre>Stream.generate(PossiblySupplier.of(() -> nis.read()))</pre>
 * 
 * will create a 
 * 
 * <pre>Stream&lt;Possibly&lt;Integer&gt;&gt</pre>
 * 
 * despite the fact that read could throw an IOException. In this case the
 * PossiblySupplier type is designed to create a 
 * 
 * <pre>Possibly&lt;T&gt;</pre>
 * 
 * so that you can further process the value, or interrogate the Exception 
 * throw further down the chain.
 * 
 * <p>Note that the type of the Possibly cannot be an exception.
 * 
 * 
 * @author randal kamradt
 * @param <T> The type of this Possibly. The exception is always of type Exception
 * @since 1.0.0
 */
public class Possibly<T> {
    /** 
     * Singleton instance of an empty Possibly
     */
    private static final Possibly<?> EMPTY = new Possibly<>(null, null);
    /**
     * The value. can be null internally, but always presented as an Optional
     */
    final T value;
    /**
     * The exception. can be null internally, but always presented as an 
     * Optional
     */
    final Exception exception;
    /**
     * create a Possibly with a value. In this case the value is checked for
     * null and, if so, will throw an exception 
     * @param value the type of the value represented. 
     */
    private Possibly(T value) {
        if(value == null) 
            throw new IllegalArgumentException("value of Possibly cannot be null");
        this.value = value;
        this.exception = null;
    }
    /**
     * create a Possibly with an exception. An exceptional Possibly can never have
     * a value
     * @param exception 
     */
    private Possibly(Exception exception) {
        if(exception == null) 
            throw new IllegalArgumentException("exception of Possibly cannot be null");
        this.value = null;
        this.exception = exception;
    }
    /**
     * create a Possibly with a value and an exception. This is used internally
     * during mapping functions and to create an empty Possibly
     * @param value
     * @param exception 
     */
    private Possibly(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }
    /**
     * Create a Possibly that represents type T. This object must contain a 
     * value and must not contain an exception
     * @param <T> The type of the Possibly to create
     * @param value The value to create it with
     * @return a new Possibly of type T and value
     */
    public static <T> Possibly<T> of(T value ) {
        return new Possibly(value);
    }
    /**
     * Create a Possibly that represents type T. This object may contain a 
     * value and must not contain an exception. if the value is null, it will
     * create an empty type.
     * @param <T> The type of the Possibly to create
     * @param value The value to create it with
     * @return a new Possibly of type T and value
     */
    public static <T> Possibly<T> ofNullable(T value ) {
        return new Possibly(value, null);
    }
    /**
     * Create an exceptional Possibly of type T. this object will never have a
     * value and the exception cannot be null
     * @param <T> The type of the Possibly to create 
     * @param exception The exception to create it with
     * @return a new exceptional Possibly of type T and exception
     */
    public static <T> Possibly<T> of(Exception exception ) {
        return new Possibly(exception);
    }
    /**
     * Return an empty Possibly of type T. this object will never have a
     * value or an exception
     * @param <T> The type of the Possibly to create 
     * @return an empty Possibly of type T and no exception
     */
    public static <T> Possibly<T> empty() {
        return (Possibly<T>)EMPTY;
    }
    /**
     * Predicate function to return if there is a value. Note
     * that this will return false if the Possibly is empty <i>or</i>
     * if it an exceptional Possibly
     * @return true if the value is present otherwise false
     */
    public boolean is() {
        return value != null;
    }
    /**
     * Predicate function to return if there is an exception
     * @return true if the value
     */
    public boolean exceptional() {
        return exception != null;
    }
    /**
     * Predicate function to check for an empty Possibly (one with no
     * value and no exception)
     * @return 
     */
    public boolean isEmpty() {
        return value == null && exception == null;
    }
    /**
     * Perform some action if there is an exception. Can be used for logging
     * or other actions that produce side-effects
     * @param action the action to perform
     * @return this
     */
    public Possibly<T> doOnException(Consumer<Exception> action) {
        if(exception != null) {
            action.accept(exception);
        }
        return this;
    }
    /**
     * get the value as an Optional
     * @return the value as Optional or Optional.empty
     */
    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }
    /**
     * get a possible exception as an Optional
     * @return the exception as an Optional or Optional.empty
     */
    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }
    /**
     * Map the value if there is one using the mapper function
     * @param <U> The new type of the Possibly
     * @param mapper a mapper function
     * @return a Possibly of type U
     */
    public <U> Possibly<U> map(Function<T, U> mapper) {
        return new Possibly(getValue().map(mapper).orElse(null), getException().orElse(null));
    }
    /**
     * Flat Map the value if there is one using the mapper function
     * @param <U> The new type of the Possibly
     * @param mapper a mapper function that returns an Optional of type U
     * @return a Possibly of type U
     */
    public <U> Possibly<U> flatMap(Function<T, ? extends Optional<? extends U>> mapper) {
        return new Possibly(getValue().flatMap(mapper).orElse(null), getException().orElse(null));
    }
    /**
     * filter base on the predicate function.
     * @param predicate the predicate function
     * @return a new Possibly that is the same as the old possibly or an empty or exceptional Possibly
     */
    public Possibly<T> filter(Predicate<T> predicate) {
        return new Possibly(getValue().filter(predicate).orElse(null), 
                getException().orElse(null));
    }
    /**
     * return the value as a Stream of zero or one item
     * @return a Stream of zero or one item
     */
    public Stream<T> stream() {
        return is() ? Stream.of(value) : Stream.empty();
    }
}
