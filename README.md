# Possibly -- Simple exception handling for lambdas in Java
Create methods that can be used as lambdas that handle exceptions in a reasonable way.

The key class is the the Possibly class which is similar to the Scala Either type. But
it is only meant to handle exceptions, so it can only contain a value or an Exception type
The Possibly class is patterned off the Optional class with many of the same functions.

The types that can be used as lambdas are

