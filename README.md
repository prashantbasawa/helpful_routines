# Helpful Routines
This project contains some useful helper classes which are handy in any projects.

## 1. TryCatch
Imagine, you are processing a stream of values and a streaming operation threw a checked exception. You will be forced to enclose those operation calls in a try-catch block, only to re-throw as unchecked exception (RuntimeException).

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