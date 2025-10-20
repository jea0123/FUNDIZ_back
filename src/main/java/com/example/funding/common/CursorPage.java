package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private final List<T> items;
    private final Cursor nextCursor;
    private final boolean hasNext;
    private final Long totalCount;

    public CursorPage(List<T> items, Cursor nextCursor) {
        this(items, nextCursor, nextCursor != null, null);
    }

    public static <T> CursorPage<T> of(List<T> items, Cursor cursor) {
        return new CursorPage<>(items, cursor);
    }

    public static <T> CursorPage<T> empty() {
        return new CursorPage<>(List.of(), null, false, 0L);
    }
}
