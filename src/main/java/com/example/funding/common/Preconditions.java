package com.example.funding.common;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public final class Preconditions {
    private Preconditions() {}

    /**
     * 조건이 false일 경우, Supplier에서 제공하는 예외를 던진다.
     * @param condition 검사할 조건
     * @param ex 예외를 제공하는 Supplier
     */
    public static void require(boolean condition, Supplier<? extends RuntimeException> ex) {
        if (!condition) throw ex.get();
    }

    /**
     * obj가 null일 경우, Supplier에서 제공하는 예외를 던진다.
     * @param obj 검사할 객체
     * @param ex 예외를 제공하는 Supplier
     * @return obj
     * @param <T> obj의 타입
     */
    public static <T> T requireNonNull(T obj, Supplier<? extends RuntimeException> ex) {
        if (obj == null) throw ex.get();
        return obj;
    }

    /**
     * s가 null이거나 빈 문자열일 경우, Supplier에서 제공하는 예외를 던진다.
     * @param s 검사할 문자열
     * @param ex 예외를 제공하는 Supplier
     * @return s
     */
    public static String requireHasText(String s, Supplier<? extends RuntimeException> ex) {
        if (s == null || s.trim().isEmpty()) throw ex.get();
        return s;
    }

    /**
     * Optional이 비어있을 경우, Supplier에서 제공하는 예외를 던진다.
     * @param opt 검사할 Optional
     * @param ex 예외를 제공하는 Supplier
     * @return Optional이 감싸고 있는 값
     * @param <T> Optional이 감싸고 있는 값의 타입
     */
    public static <T> T requireFound(Optional<T> opt, Supplier<? extends RuntimeException> ex) {
        return opt.orElseThrow(ex);
    }

    /**
     * n이 null이거나 0 이하일 경우, Supplier에서 제공하는 예외를 던진다.
     * @param n 검사할 숫자
     * @param ex 예외를 제공하는 Supplier
     */
    public static void requirePositive(Number n, Supplier<? extends RuntimeException> ex) {
        if (n == null || n.longValue() <= 0) throw ex.get();
    }

    /**
     * value가 allowed에 포함되어 있지 않을 경우, Supplier에서 제공하는 예외를 던진다.
     * @param value 검사할 값
     * @param allowed 허용된 값들의 컬렉션
     * @param ex 예외를 제공하는 Supplier
     * @param <E> value와 allowed의 요소 타입
     */
    public static <E> void requireIn(E value, Collection<E> allowed, Supplier<? extends RuntimeException> ex) {
        if (value == null || !allowed.contains(value)) throw ex.get();
    }
}