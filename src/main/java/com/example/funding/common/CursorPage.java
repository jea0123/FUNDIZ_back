package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private final List<T> items;
    private final Cursor nextCursor;
    private final boolean hasNext;

    public CursorPage(List<T> items, Cursor nextCursor) {
        this(items, nextCursor, nextCursor != null);
    }

    public static <T> CursorPage<T> of(List<T> items, Cursor cursor) {
        return new CursorPage<>(items, cursor);
    }

    /**
     * size+1로 받아온 window에서 딱 size개만 잘라 내고,
     * 마지막 아이템 기준으로 nextCursor/hasNext를 계산.
     */
    public static <T> CursorPage<T> fromWindow(
            List<T> window,
            int size,
            Function<T, LocalDateTime> createdAtFn,
            Function<T, Long> idFn
    ) {
        boolean hasNext = window.size() > size;
        List<T> items = hasNext ? window.subList(0, size) : window;

        Cursor cursor = null;
        if (hasNext && !items.isEmpty()) {
            T last = items.getLast();
            cursor = new Cursor(createdAtFn.apply(last), idFn.apply(last));
        }
        return new CursorPage<>(items, cursor, hasNext);
    }
}
