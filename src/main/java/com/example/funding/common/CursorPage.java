package com.example.funding.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CursorPage<T> {
    private final List<T> items;
    private final Cursor nextCursor;

    public static <T> CursorPage<T> of(List<T> items, Cursor cursor) {
        return new CursorPage<>(items, cursor);
    }
}
