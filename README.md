# Helpful Routines
This project contains some useful helper classes which are handy in any projects.

## Table of Contents
1. [TryCatch](#TryCatch)
1. [MdcThreadHelper](#MdcThreadHelper)

## 1. TryCatch <a name="TryCatch"></a>
Imagine, you are processing a stream of values and a streaming operation threw a checked exception. You will be forced to enclose those operation calls in a try-catch block, 
only to re-throw as unchecked exception (RuntimeException).
```TryCatch.wrap()``` methods help in reducing this verbose code in an elegant way.

### Before
```java
List<String> fileData = files.stream()
    .map(f -> try { 
            return FileUtils.readFileToString(f, "UTF-8"); 
        } catch(IOException io) {
            throw new RuntimeException(io);
        }   
    )
    .collect(Collectors.toList())
```

### After
```java
List<String> fileData = files.stream()
    .map(TryCatch.wrap(f -> FileUtils.readFileToString(f, "UTF-8")))
    .collect(Collectors.toList())
```

Similarly, there are overloaded ```TryCatch.wrap()``` methods for Suppliers and Consumers as well. Check them out.

## 2. MdcThreadHelper <a name="MdcThreadHelper"></a>
Suppose, you have a vanilla tracing mechanism where you use MDC of a logging framework to trace calls. 
Since, MDCs are associated with a calling thread, anytime, we spawn additional threads for processing,
those additional threads will loose the tracing info. In such situations, we must transfer the tracing information
from calling thread to other spawned threads. This helper class exactly does that. 

### Before
```java
CompletableFuture<PersonData> personDataCall = CompletableFuture.supplyAsynch(() -> getPersonData(key));
CompletableFuture<PersonTaxData> personTaxDataCall = CompletableFuture.supplyAsynch(() -> getPersonTaxData(key));

CompletableFuture.allOf(personDataCall, personTaxDataCall).join();
```

### After
```java
CompletableFuture<PersonData> personDataCall = CompletableFuture.supplyAsynch(MdcThreadHelper.tracedSupplier(() -> getPersonData(key)));
CompletableFuture<PersonTaxData> personTaxDataCall = CompletableFuture.supplyAsynch(MdcThreadHelper.tracedSupplier(() -> getPersonTaxData(key)));

CompletableFuture.allOf(personDataCall, personTaxDataCall).join();
```

Similarly, ```MdcThreadHelper``` has methods for ```Callable``` and ```Runnable``` calls. Check them out.