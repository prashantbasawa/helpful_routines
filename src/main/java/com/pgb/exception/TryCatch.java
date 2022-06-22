package com.pgb.exception;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class TryCatch {

    private static final Consumer<Throwable> DEFAULT_EXCEPTION_TRANSLATOR = throwable -> {
        if(throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    };

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> {
        R apply(T t) throws Throwable;
    }

    public static <T> Supplier<T> wrap(ThrowingSupplier<T> throwingSupplier) {
        return wrap(throwingSupplier, DEFAULT_EXCEPTION_TRANSLATOR);
    }

    public static <T> Supplier<T> wrap(ThrowingSupplier<T> throwingSupplier, Consumer<Throwable> exceptionTranslator) {
        return () -> {
            try {
                return throwingSupplier.get();
            } catch (Throwable throwable) {
                exceptionTranslator.accept(throwable);

                throw new IllegalStateException("Exception translator must throw a runtime exception");
            }
        };
    }

    public static <T> Consumer<T> wrap(ThrowingConsumer<T> throwingConsumer) {
        return wrap(throwingConsumer, DEFAULT_EXCEPTION_TRANSLATOR);
    }

    public static <T> Consumer<T> wrap(ThrowingConsumer<T> throwingConsumer, Consumer<Throwable> exceptionTranslator) {
        return t -> {
            try {
                throwingConsumer.accept(t);
            } catch (Throwable throwable) {
                exceptionTranslator.accept(throwable);

                throw new IllegalStateException("Exception translator must throw a runtime exception");
            }
        };
    }

    public static <T, R> Function<T, R> wrap(ThrowingFunction<T, R> throwingFunction) {
        return wrap(throwingFunction, DEFAULT_EXCEPTION_TRANSLATOR);
    }

    public static <T, R> Function<T, R> wrap(ThrowingFunction<T, R> throwingFunction, Consumer<Throwable> exceptionTranslator) {
        return t -> {
            try {
                return throwingFunction.apply(t);
            } catch (Throwable throwable) {
                exceptionTranslator.accept(throwable);

                throw new IllegalStateException("Exception translator must throw a runtime exception");
            }
        };
    }
}
