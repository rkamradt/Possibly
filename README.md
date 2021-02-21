# Possibly -- Simple exception handling for lambdas in Java

[![Build status](https://ci.appveyor.com/api/projects/status/6e838jmqtxlgwevr?svg=true)](https://ci.appveyor.com/project/rkamradt/possibly)

Create methods that can be used as lambdas that handle exceptions in a reasonable way.
*** Possibly Class
The key class is the the Possibly class which is similar to the Scala Either type. But
it is only meant to handle exceptions, so it can only contain a value or an Exception type
The Possibly class is patterned off the Optional class with many of the same functions.
*** Functional Interfaces
The types that can be used as lambdas are

