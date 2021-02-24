# Possibly -- Simple exception handling for lambdas in Java

[![Build status](https://ci.appveyor.com/api/projects/status/6e838jmqtxlgwevr?svg=true)](https://ci.appveyor.com/project/rkamradt/possibly)

Create methods that can be used as lambdas that handle exceptions in a reasonable way.
### Possibly Class
The key class is the the Possibly class which is similar to the Scala Either type. But
it is only meant to handle exceptions, so it can only contain a value or an Exception type
The Possibly class is patterned off the Optional class with many of the same functions.
### Functional Interfaces
The types that can be used as lambdas are

- PossiblyConsumer
- PossiblyFunction
- PossiblyPredicate
- PossiblySupplier

Note that only the PossiblyFunction and PossiblySupplier return a Possibly type. 
PossiblyConsumer returns nothing and PossiblyPredicate returns Boolean, in both
of those cases, a Consumer<Exception> is part of the builder so you can handle 
any exceptions. Your Consumer<Exception> can throw a RuntimeException or other
uncaught exception to fail the process. If you don't throw another uncaught 
exception as part of the PossiblyPredicate Consmer<Exception> it will return
false after running the consumer (future enhancement determin what PossiblyPredicate
will return)

Example usages:

PossiblyConsumer:
```
        Stream.of(GOOD_VALUE, BAD_VALUE)
                .peek(PossiblyConsumer.of(s -> mapWithException(s), e -> logError(e)))
```
 
In this case the mapWithException function will throw an Exception on a BAD_VALUE
in which case it will log the error.

PossiblyFunction:
```
        List<String> list = Stream.of(GOOD_VALUE, BAD_VALUE)
                .map(PossiblyFunction.of(s -> mapWithException(s)))
                .flatMap(p -> p.getValue()) // will return Optional.empty on badvalue
                .collect(Collectors.toList());
```

PossiblyPredicate:
```
        List<String> list = Stream.of(GOOD_VALUE, BAD_VALUE)
                .filter(PossiblyPredicate.of(s -> mapWithException(s), e -> logError(e)))
                .collect(Collectors.toList());
```

PossiblySupplier:
```
        List<Possibly<Integer>> list = 
                Stream.generate(PossiblySupplier.of(() -> supplyWithException()))
                .limit(4)
                .collect(Collectors.toList());
```

In this case supplyWithException will return incremental int values starting
with 0 and will throw an exception on odd values.

### Usage
to use this library add the following to your pom.xml <depenedencies>:

```
        <dependency>
            <groupId>io.github.rkamradt</groupId>
            <artifactId>possibly</artifactId>
            <version>1.0.1</version>
        </dependency>
``` 
